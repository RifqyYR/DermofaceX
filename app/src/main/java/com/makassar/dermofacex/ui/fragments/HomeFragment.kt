package com.makassar.dermofacex.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
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