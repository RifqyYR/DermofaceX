package com.makassar.dermofacex.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.makassar.dermofacex.R
import com.makassar.dermofacex.data.Resource
import com.makassar.dermofacex.databinding.FragmentHomeBinding
import com.makassar.dermofacex.ui.viewModel.MainViewModel
import com.makassar.dermofacex.utils.disorderInformation
import com.makassar.dermofacex.utils.getFileFromUri
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModel()
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    var isResultShown = false // Flag untuk mengontrol satu kali tampil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupButton()

        setupGalleryLauncher()
        getClassificationResult()
    }

    private fun setupGalleryLauncher() {
        // Initialize the ActivityResultLauncher
        galleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    // Kirim gambar ke API dan navigasi ke fragment baru
                    uploadImageAndShowResult(uri)
                }
            }
        }
    }

    private fun showImageSourceDialog() {
        val options = arrayOf(getString(R.string.camera), getString(R.string.gallery))
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.select_image_source))
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> {
                    // Navigasi ke CameraFragment
                    findNavController().navigate(R.id.action_homeFragment_to_cameraFragment)
                }

                1 -> {
                    // Pilih gambar dari galeri
                    pickImageFromGallery()
                }
            }
        }
        builder.show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.loadingOverlay.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.loadingOverlay.visibility = View.GONE
        }
    }

    private fun uploadImageAndShowResult(imageUri: Uri) {
        // Convert URI ke file atau inputStream sesuai kebutuhan API
        val imageFile = getFileFromUri(requireContext(), imageUri)

        val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)
        viewModel.getClassifyResult(imagePart)
    }

    private fun getClassificationResult() {
        lifecycleScope.launch {
            viewModel.classify.collectLatest { result ->
                when (result) {
                    is Resource.Empty -> {
                        showLoading(false)
                    }

                    is Resource.Error -> {
                        showLoading(false)
                        Toast.makeText(
                            requireContext(),
                            "Terjadi Kesalahan",
                            Toast.LENGTH_SHORT
                        ).show()
                        isResultShown = false
                    }

                    is Resource.Loading -> showLoading(true)
                    is Resource.Success -> {
                        showLoading(false)
                        val bundle = Bundle()
                        val bottomSheetResult = BottomSheetResultFragment()

                        bundle.putString(
                            BottomSheetResultFragment.DISORDER_NAME,
                            result.data?.result
                        )
                        bottomSheetResult.arguments = bundle
                        bottomSheetResult.show(
                            requireActivity().supportFragmentManager,
                            bottomSheetResult.tag
                        )
                        isResultShown = true
                    }
                }
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }

    private fun setupButton() {
        val normalInfo = disorderInformation[5]
        val oilyInfo = disorderInformation[4]
        val blackheadsInfo = disorderInformation[3]
        val rednessInfo = disorderInformation[2]
        val hyperpigmentationInfo = disorderInformation[0]
        val acneInfo = disorderInformation[1]

        binding.cardCheckYourFaceSkin.setOnClickListener {
            showImageSourceDialog()
        }

        // Navigasi untuk "Normal" card
        binding.cardNormal.card.setOnClickListener {
            val bundle = bundleOf(
                "disorderInformation" to normalInfo, // Mengirimkan objek DisorderInformation
                "image" to R.drawable.normal // Gambar tetap dari drawable resource
            )
            findNavController().navigate(
                R.id.action_homeFragment_to_detailFacialSkinDisorderFragment,
                bundle
            )
        }

        // Navigasi untuk "Oily" card
        binding.cardOily.card.setOnClickListener {
            val bundle = bundleOf(
                "disorderInformation" to oilyInfo,
                "image" to R.drawable.oily
            )
            findNavController().navigate(
                R.id.action_homeFragment_to_detailFacialSkinDisorderFragment,
                bundle
            )
        }

        // Navigasi untuk "Blackheads" card
        binding.cardBlackheads.card.setOnClickListener {
            val bundle = bundleOf(
                "disorderInformation" to blackheadsInfo,
                "image" to R.drawable.komedo
            )
            findNavController().navigate(
                R.id.action_homeFragment_to_detailFacialSkinDisorderFragment,
                bundle
            )
        }

        // Navigasi untuk "Redness" card
        binding.cardRedness.card.setOnClickListener {
            val bundle = bundleOf(
                "disorderInformation" to rednessInfo,
                "image" to R.drawable.kemerahan
            )
            findNavController().navigate(
                R.id.action_homeFragment_to_detailFacialSkinDisorderFragment,
                bundle
            )
        }

        // Navigasi untuk "Hyperpigmentation" card
        binding.cardHyperpigmentation.card.setOnClickListener {
            val bundle = bundleOf(
                "disorderInformation" to hyperpigmentationInfo,
                "image" to R.drawable.hyperpigmentation
            )
            findNavController().navigate(
                R.id.action_homeFragment_to_detailFacialSkinDisorderFragment,
                bundle
            )
        }

        // Navigasi untuk "Acne" card
        binding.cardAcne.card.setOnClickListener {
            val bundle = bundleOf(
                "disorderInformation" to acneInfo,
                "image" to R.drawable.acne
            )
            findNavController().navigate(
                R.id.action_homeFragment_to_detailFacialSkinDisorderFragment,
                bundle
            )
        }

        binding.btnApplicationInformation.btn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_aboutAppFragment)
        }
        binding.btnHowToUse.btn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_howToUseFragment)
        }
    }

    private fun setupUI() {
        // Ambil informasi dari array disorderInformation
        val acneInfo = disorderInformation[1] // Index untuk Jerawat
        val hyperpigmentationInfo = disorderInformation[0] // Index untuk Hiperpigmentasi
        val rednessInfo = disorderInformation[2] // Index untuk Kemerahan
        val blackheadsInfo = disorderInformation[3] // Index untuk Komedo
        val oilyInfo = disorderInformation[4] // Index untuk Berminyak
        val normalInfo = disorderInformation[5] // Index untuk Normal

        // Set data untuk card Acne
        binding.cardAcne.apply {
            tvCard.text = acneInfo.name // Menggunakan data dari disorderInformation
            ivCard.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.acne, // Gambar dari resource
                    null
                )
            )
        }

        // Set data untuk card Hyperpigmentation
        binding.cardHyperpigmentation.apply {
            tvCard.text = hyperpigmentationInfo.name
            ivCard.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.hyperpigmentation,
                    null
                )
            )
        }

        // Set data untuk card Redness
        binding.cardRedness.apply {
            tvCard.text = rednessInfo.name
            ivCard.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.kemerahan,
                    null
                )
            )
        }

        // Set data untuk card Blackheads
        binding.cardBlackheads.apply {
            tvCard.text = blackheadsInfo.name
            ivCard.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.komedo,
                    null
                )
            )
        }

        // Set data untuk card Oily
        binding.cardOily.apply {
            tvCard.text = oilyInfo.name
            ivCard.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.oily,
                    null
                )
            )
        }

        // Set data untuk card Normal
        binding.cardNormal.apply {
            tvCard.text = normalInfo.name
            ivCard.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.normal,
                    null
                )
            )
        }

        // Application Information UI
        binding.btnHowToUse.apply {
            tvButton.text = getString(R.string.how_to_use)
            icButton.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.baseline_info_24,
                    null
                )
            )
        }
        binding.btnApplicationInformation.apply {
            tvButton.text = getString(R.string.about_application)
            icButton.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.baseline_phone_android_24,
                    null
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}