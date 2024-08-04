package com.makassar.dermofacex.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.makassar.dermofacex.R
import com.makassar.dermofacex.databinding.FragmentDetailFacialSkinDisorderBinding

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

        val image = arguments?.getInt("image")
        val facialSkinDisorder = arguments?.getString("facialSkinDisorder")
        val definition = arguments?.getInt("definition")
        val cause = arguments?.getInt("cause")
        val treatment = arguments?.getInt("treatment")
        val prevention = arguments?.getInt("prevention")

        setupUI(image, facialSkinDisorder, definition, cause, treatment, prevention)

        setupButton()
    }

    private fun setupUI(
        image: Int?,
        facialSkinDisorder: String?,
        definition: Int?,
        cause: Int?,
        treatment: Int?,
        prevention: Int?
    ) {
        if (facialSkinDisorder == "Normal") {
            binding.dropdownTreatment.visibility = View.GONE
            binding.tvDefinition.visibility = View.VISIBLE
            binding.dropdownPrevention.visibility = View.GONE
            binding.ivFacialSkinDisorder.setImageResource(image!!)
            binding.tvFacialSkinDisorderTitle.text = facialSkinDisorder
            binding.tvDefinition.text = getString(definition!!)
            binding.tvCause.text = getString(cause!!)
        }
        binding.ivFacialSkinDisorder.setImageResource(image!!)
        binding.tvFacialSkinDisorderTitle.text = facialSkinDisorder
        binding.tvDefinition.text = getString(definition!!)
        binding.tvCause.text = getString(cause!!)
        binding.tvTreatment.text = getString(treatment!!)
        binding.tvPrevention.text = getString(prevention!!)
    }

    private fun setupButton() {
        binding.dropdownPrevention.setOnClickListener {
            if (binding.tvPrevention.visibility == View.VISIBLE) {
                binding.tvPrevention.visibility = View.GONE
                binding.ivArrowPrevention.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.baseline_keyboard_arrow_down_24
                    )
                )
            } else {
                binding.tvPrevention.visibility = View.VISIBLE
                binding.ivArrowPrevention.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.baseline_keyboard_arrow_up_24
                    )
                )
            }
        }
        binding.dropdownTreatment.setOnClickListener {
            if (binding.tvTreatment.visibility == View.VISIBLE) {
                binding.tvTreatment.visibility = View.GONE
                binding.ivArrowTreatment.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.baseline_keyboard_arrow_down_24
                    )
                )
            } else {
                binding.tvTreatment.visibility = View.VISIBLE
                binding.ivArrowTreatment.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.baseline_keyboard_arrow_up_24
                    )
                )
            }
        }
        binding.dropdownCause.setOnClickListener {
            if (binding.tvCause.visibility == View.VISIBLE) {
                binding.tvCause.visibility = View.GONE
                binding.ivArrowCause.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.baseline_keyboard_arrow_down_24
                    )
                )
            } else {
                binding.tvCause.visibility = View.VISIBLE
                binding.ivArrowCause.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.baseline_keyboard_arrow_up_24
                    )
                )
            }
        }

        binding.btnBack.btn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}