package com.makassar.dermofacex.ui.fragments.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.makassar.dermofacex.data.ClassificationProbabilities
import com.makassar.dermofacex.databinding.FragmentBottomSheetProbabilityResultBinding
import com.makassar.dermofacex.ui.adapters.ProbabilityPagerAdapter

class BottomSheetProbabilityResultFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetProbabilityResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetProbabilityResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the bottom sheet behavior
        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.peekHeight = (resources.displayMetrics.heightPixels * 0.4).toInt()

        val classificationProbability =
            arguments?.getSerializable(CLASSIFICATION_PROBABILITY) as? ClassificationProbabilities
        classificationProbability?.let { setupViewPager(it) }
    }

    private fun setupViewPager(classificationProb: ClassificationProbabilities) {
        // Buat adapter untuk ViewPager2
        val adapter = ProbabilityPagerAdapter(
            this, classificationProb
        )
        binding.viewPager.adapter = adapter

        // Hubungkan ViewPager2 dengan TabLayout
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "XGB"
                1 -> tab.text = "CatBoost"
                2 -> tab.text = "LightGBM"
            }
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val CLASSIFICATION_PROBABILITY = "CLASSIFICATION_PROBABILITY"
    }
}