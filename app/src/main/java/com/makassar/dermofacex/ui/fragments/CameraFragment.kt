package com.makassar.dermofacex.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.PointF
import android.os.Bundle
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
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.common.InputImage
import com.makassar.dermofacex.R
import com.makassar.dermofacex.databinding.FragmentCameraBinding
import com.makassar.dermofacex.utils.FaceDetectionProcessor
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.sqrt

class CameraFragment : Fragment() {
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private val faceDetectionProcessor = FaceDetectionProcessor()
    val faceTracker = mutableMapOf<Int, Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        outputDirectory = getOutputDirectory()
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
                                        // takePhoto()
                                        binding.tvStatus.text = getString(R.string.face_detected)
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

            // Select back camera as a default
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

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            "${System.currentTimeMillis()}.jpg"
        )

        // Setup image capture listener which is triggered after photo has been taken
        imageCapture.takePicture(
            ImageCapture.OutputFileOptions.Builder(photoFile).build(),
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: photoFile.toUri()
                    Log.d(TAG, "Photo capture succeeded: $savedUri")
                }
            })
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireContext().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireContext().filesDir
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
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
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