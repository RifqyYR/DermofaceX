package com.makassar.dermofacex.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.makassar.dermofacex.data.ClassificationProbabilities
import com.makassar.dermofacex.ui.fragments.probabilityTab.CBResultFragment
import com.makassar.dermofacex.ui.fragments.probabilityTab.LGBResultFragment
import com.makassar.dermofacex.ui.fragments.probabilityTab.XGBResultFragment

class ProbabilityPagerAdapter(
    fragment: Fragment,
    private val classificationProbability: ClassificationProbabilities
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> classificationProbability.classProbabilitiesXgb.let { XGBResultFragment(it) }
            1 -> classificationProbability.classProbabilitiesCb.let { CBResultFragment(it) }
            2 -> classificationProbability.classProbabilitiesLgb.let { LGBResultFragment(it) }
            else -> classificationProbability.classProbabilitiesXgb.let { XGBResultFragment(it) }
        }
    }
}