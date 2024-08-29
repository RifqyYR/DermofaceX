package com.makassar.dermofacex.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.PointF
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.common.InputImage
import com.makassar.dermofacex.R
import com.makassar.dermofacex.data.Resource
import com.makassar.dermofacex.databinding.FragmentCameraBinding
import com.makassar.dermofacex.di.viewModelModule
import com.makassar.dermofacex.ui.viewModel.MainViewModel
import com.makassar.dermofacex.utils.FaceDetectionProcessor
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import java.io.File
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.sqrt

class CameraFragment : Fragment() {
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModel()
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private val faceDetectionProcessor = FaceDetectionProcessor()
    val faceTracker = mutableMapOf<Int, Int>()
    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis = 5000L // 5 seconds

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadKoinModules(viewModelModule)
        cameraExecutor = Executors.newSingleThreadExecutor()

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        getClassificationResult()

        outputDirectory = getOutputDirectory()
    }

    private val takePhotoRunnable = object : Runnable {
        override fun run() {
            takePhoto()
            binding.tvStatus.text = getString(R.string.face_detected)
        }
    }

    @OptIn(ExperimentalGetImage::class)
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        val imageAnalysis = ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(cameraExecutor) { imageProxy ->
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                        val image = InputImage.fromMediaImage(mediaImage, rotationDegrees)

                        faceDetectionProcessor.process(image, { faces ->
                            for (face in faces) {
                                val bounds = face.boundingBox
                                val faceCenter =
                                    PointF(bounds.centerX().toFloat(), bounds.centerY().toFloat())

                                val headEulerAngleY =
                                    face.headEulerAngleY // Yaw angle (rotation around y-axis)
                                val headEulerAngleZ =
                                    face.headEulerAngleZ // Roll angle (rotation around z-axis)

                                // Get the oval bounds
                                val ovalBounds = binding.ovalView.getOvalRect()

                                val distanceFromCenter = sqrt(
                                    (((faceCenter.x - ovalBounds.centerX()) * (faceCenter.x - ovalBounds.centerX())) / (ovalBounds.width() / 2 * ovalBounds.width() / 2) +
                                            ((faceCenter.y - ovalBounds.centerY()) * (faceCenter.y - ovalBounds.centerY())) / (ovalBounds.height() / 2 * ovalBounds.height() / 2)).toDouble()
                                )

                                // Check if the center of the face's bounding box is within the oval
                                if (distanceFromCenter in 0.58F..0.62F && (abs(headEulerAngleY) < 2.5 && abs(
                                        headEulerAngleZ
                                    ) < 30)
                                ) {
                                    // The face is within the oval, capture the image
                                    val trackingId = face.trackingId ?: return@process
                                    faceTracker[trackingId] =
                                        faceTracker.getOrDefault(trackingId, 0) + 1

                                    // If the face has been detected in 3 consecutive frames, consider it a real face
                                    if (faceTracker[trackingId]!! >= 3) {
                                        // Remove any previous callbacks to prevent multiple executions
                                        handler.removeCallbacks(takePhotoRunnable)

                                        // Post the Runnable to be executed after the delay
                                        handler.postDelayed(takePhotoRunnable, delayMillis)
                                    }
                                } else {
                                    binding.tvStatus.text =
                                        getString(R.string.position_your_face_in_the_oval)
                                }
                            }

                            imageProxy.close()
                        }, { e ->
                            binding.tvStatus.text =
                                getString(R.string.position_your_face_in_the_oval)
                            Log.e(TAG, "Face detection failed", e)
                            imageProxy.close()
                        })
                    }
                }
            }

        cameraProviderFuture.addListener({

            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            // Select front camera as a default
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture,
                    imageAnalysis
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(outputDirectory, "${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = photoFile.toUri()
                    sendImageToAPI(savedUri)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraFragment", "Photo capture failed: ${exception.message}", exception)
                }
            })
    }

    private fun sendImageToAPI(image: Uri) {
        val fileBytes = readFileFromUri(image) ?: return
        val file = File(requireContext().cacheDir, "uploaded_image.jpg").apply {
            writeBytes(fileBytes)
        }
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        viewModel.getClassifyResult(body)
    }

    private fun readFileFromUri(uri: Uri): ByteArray? {
        val contentResolver = requireContext().contentResolver
        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.readBytes()
            }
        } catch (e: IOException) {
            Log.e("CameraFragment", "Failed to read file from URI: ${e.message}", e)
            null
        }
    }

    private fun getClassificationResult() {
        lifecycleScope.launch {
            viewModel.classify.collectLatest { result ->
                when (result) {
                    is Resource.Empty -> binding.tvClassificationResult.text = "Tidak Ditemukan"
                    is Resource.Error -> {
                        Toast.makeText(
                            requireContext(),
                            "Terjadi Kesalahan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is Resource.Loading -> binding.tvClassificationResult.text = "Memproses citra"
                    is Resource.Success -> binding.tvClassificationResult.text = result.data?.result
                }
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return mediaDir?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        } ?: File(requireContext().filesDir, resources.getString(R.string.app_name))
    }

    // checks the camera permission
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                )
                    .show()
                findNavController().popBackStack()
            }
        }
    }

    companion object {
        private const val TAG = "CameraXGFG"
        private const val REQUEST_CODE_PERMISSIONS = 20
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val cameraProvider = ProcessCameraProvider.getInstance(requireContext()).get()
        cameraProvider.unbindAll()
        cameraExecutor.shutdown()
        _binding = null
        imageCapture = null
    }
}