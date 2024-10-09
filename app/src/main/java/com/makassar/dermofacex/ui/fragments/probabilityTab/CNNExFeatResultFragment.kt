package com.makassar.dermofacex.ui.fragments.probabilityTab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.makassar.dermofacex.data.response.ClassProbabilitiesCnnExFeat
import com.makassar.dermofacex.databinding.FragmentProbabilityResultTabBinding

class CNNExFeatResultFragment(private val probabilities: ClassProbabilitiesCnnExFeat) : Fragment() {
    private var _binding: FragmentProbabilityResultTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProbabilityResultTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI(probabilities)
    }

    private fun setupUI(probabilities: ClassProbabilitiesCnnExFeat) {
        binding.tvAcneResultProbability.text = buildString {
            append("Jerawat: ")
            append(probabilities.jerawat)
        }
        binding.tvHyperpigmentationResultProbability.text = buildString {
            append("Hiperpigmentasi: ")
            append(probabilities.hiperpigmentasi)
        }
        binding.tvRednessResultProbability.text = buildString {
            append("Kemerahan: ")
            append(probabilities.kemerahan)
        }
        binding.tvOilyResultProbability.text = buildString {
            append("Berminyak: ")
            append(probabilities.berminyak)
        }
        binding.tvBlackheadsResultProbability.text = buildString {
            append("Komedo: ")
            append(probabilities.komedo)
        }
        binding.tvNormalResultProbability.text = buildString {
            append("Normal: ")
            append(probabilities.normal)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}