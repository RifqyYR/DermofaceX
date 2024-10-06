package com.makassar.dermofacex.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.makassar.dermofacex.data.DisorderInformation
import com.makassar.dermofacex.databinding.FragmentDetailFacialSkinDisorderBinding
import com.makassar.dermofacex.ui.adapters.DetailPagerAdapter

class DetailFacialSkinDisorderFragment : Fragment() {
    private var _binding: FragmentDetailFacialSkinDisorderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailFacialSkinDisorderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil data dari arguments
        val disorder = arguments?.getSerializable("disorderInformation") as? DisorderInformation
        val image = arguments?.getInt("image")

        // Cek apakah data tersedia
        if (disorder != null && image != null) {
            setupUI(disorder, image)
            if (disorder.name !== "Normal") {
                setupViewPager(disorder)
            }
        }

        setupButton()
    }

    private fun setupUI(disorder: DisorderInformation, image: Int?) {
        // Menampilkan data dari object DisorderInformation
        binding.ivFacialSkinDisorder.setImageResource(image!!) // Ambil dari arguments
        binding.tvFacialSkinDisorderTitle.text = disorder.name
        binding.tvDefinition.text = disorder.definition
    }

    private fun setupViewPager(disorder: DisorderInformation) {
        // Buat adapter untuk ViewPager2
        val adapter =
            DetailPagerAdapter(
                this,
                disorder.cause,
                disorder.treatment,
                disorder.avoided_ingredients
            )
        binding.viewPager.adapter = adapter

        // Hubungkan ViewPager2 dengan TabLayout
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Penyebab"
                1 -> tab.text = "Penanganan"
                2 -> tab.text = "Bahan Berbahaya"
            }
        }.attach()
    }

    private fun setupButton() {
        binding.btnBack.btn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}