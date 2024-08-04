package com.makassar.dermofacex.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.makassar.dermofacex.R
import com.makassar.dermofacex.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
    }

    private fun setupButton() {
        binding.cardNormal.card.setOnClickListener {
            val bundle = bundleOf(
                "image" to R.drawable.normal,
                "facialSkinDisorder" to getString(R.string.normal),
                "definition" to R.string.normal_definition,
                "cause" to R.string.normal_cause,
                "treatment" to R.string.normal,
                "prevention" to R.string.normal,
            )
            findNavController().navigate(
                R.id.action_homeFragment_to_detailFacialSkinDisorderFragment,
                bundle
            )
        }
        binding.cardOily.card.setOnClickListener {
            val bundle = bundleOf(
                "image" to R.drawable.oily,
                "facialSkinDisorder" to getString(R.string.oily),
                "definition" to R.string.oily_definition,
                "cause" to R.string.oily_cause,
                "treatment" to R.string.oily_treatment,
                "prevention" to R.string.oily_prevention,
            )
            findNavController().navigate(
                R.id.action_homeFragment_to_detailFacialSkinDisorderFragment,
                bundle
            )
        }
        binding.cardBlackheads.card.setOnClickListener {
            val bundle = bundleOf(
                "image" to R.drawable.komedo,
                "facialSkinDisorder" to getString(R.string.blackheads),
                "definition" to R.string.blackheads_definition,
                "cause" to R.string.blackheads_cause,
                "treatment" to R.string.blackheads_treatment,
                "prevention" to R.string.blackheads_prevention,
            )
            findNavController().navigate(
                R.id.action_homeFragment_to_detailFacialSkinDisorderFragment,
                bundle
            )
        }
        binding.cardRedness.card.setOnClickListener {
            val bundle = bundleOf(
                "image" to R.drawable.kemerahan,
                "facialSkinDisorder" to getString(R.string.redness),
                "definition" to R.string.redness_definition,
                "cause" to R.string.redness_cause,
                "treatment" to R.string.redness_treatment,
                "prevention" to R.string.redness_prevention,
            )
            findNavController().navigate(
                R.id.action_homeFragment_to_detailFacialSkinDisorderFragment,
                bundle
            )
        }
        binding.cardHyperpigmentation.card.setOnClickListener {
            val bundle = bundleOf(
                "image" to R.drawable.hyperpigmentation,
                "facialSkinDisorder" to getString(R.string.hyperpigmentation),
                "definition" to R.string.hyperpigmentation_definition,
                "cause" to R.string.hyperpigmentation_cause,
                "treatment" to R.string.hyperpigmentation_treatment,
                "prevention" to R.string.hyperpigmentation_prevention,
            )
            findNavController().navigate(
                R.id.action_homeFragment_to_detailFacialSkinDisorderFragment,
                bundle
            )
        }
        binding.cardAcne.card.setOnClickListener {
            val bundle = bundleOf(
                "image" to R.drawable.acne,
                "facialSkinDisorder" to getString(R.string.acne),
                "definition" to R.string.acne_definition,
                "cause" to R.string.acne_cause,
                "treatment" to R.string.acne_treatment,
                "prevention" to R.string.acne_prevention,
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
        // Facial Skin Disorder Information UI
        binding.cardAcne.apply {
            tvCard.text = getString(R.string.acne)
            ivCard.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.acne,
                    null
                )
            )
        }
        binding.cardHyperpigmentation.apply {
            tvCard.text = getString(R.string.hyperpigmentation)
            ivCard.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.hyperpigmentation,
                    null
                )
            )
        }
        binding.cardRedness.apply {
            tvCard.text = getString(R.string.redness)
            ivCard.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.kemerahan,
                    null
                )
            )
        }
        binding.cardBlackheads.apply {
            tvCard.text = getString(R.string.blackheads)
            ivCard.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.komedo,
                    null
                )
            )
        }
        binding.cardOily.apply {
            tvCard.text = getString(R.string.oily)
            ivCard.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.oily,
                    null
                )
            )
        }
        binding.cardNormal.apply {
            tvCard.text = getString(R.string.normal)
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