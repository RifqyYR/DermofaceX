package com.makassar.dermofacex.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.makassar.dermofacex.R
import com.makassar.dermofacex.databinding.FragmentScanFaceBinding

class ScanFaceFragment : Fragment() {
    private var _binding: FragmentScanFaceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanFaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButton()
    }

    private fun setupButton() {
        binding.ivFrontFace.setOnClickListener {
            findNavController().navigate(R.id.action_scanFaceFragment_to_cameraFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}