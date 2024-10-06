package com.makassar.dermofacex.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.makassar.dermofacex.data.DisorderInformation
import com.makassar.dermofacex.databinding.FragmentBottomSheetResultBinding
import com.makassar.dermofacex.utils.disorderInformation

class BottomSheetResultFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the bottom sheet behavior
        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.peekHeight = (resources.displayMetrics.heightPixels * 0.4).toInt()

        val disorderName = arguments?.getString(DISORDER_NAME)
        val disorder: DisorderInformation = disorderInformation.find { it.name == disorderName }!!
        setupUI(disorder)
    }

    private fun setupUI(disorder: DisorderInformation) {
        binding.title.text = disorder.name
        binding.tvDefinition.text = disorder.definition
        binding.tvCause.text = disorder.cause
        binding.tvPrevention.text = disorder.prevention
        binding.tvAvoidedIngredients.text =
            disorder.avoided_ingredients.joinToString(separator = "\n") { "• $it" }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val DISORDER_NAME = "DISORDER_NAME"
    }
}