package com.makassar.dermofacex.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.makassar.dermofacex.data.ClassificationProbabilities
import com.makassar.dermofacex.ui.fragments.probabilityTab.CBResultFragment
import com.makassar.dermofacex.ui.fragments.probabilityTab.CNNExFeatResultFragment
import com.makassar.dermofacex.ui.fragments.probabilityTab.CNNResultFragment
import com.makassar.dermofacex.ui.fragments.probabilityTab.LGBResultFragment
import com.makassar.dermofacex.ui.fragments.probabilityTab.XGBResultFragment
import com.makassar.dermofacex.ui.fragments.probabilityTab.YoloResultFragment

class ProbabilityPagerAdapter(
    fragment: Fragment,
    private val classificationProbability: ClassificationProbabilities
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 6
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> classificationProbability.classProbabilitiesXgb.let { XGBResultFragment(it) }
            1 -> classificationProbability.classProbabilitiesCb.let { CBResultFragment(it) }
            2 -> classificationProbability.classProbabilitiesLgb.let { LGBResultFragment(it) }
            3 -> classificationProbability.classProbabilitiesCnn.let { CNNResultFragment(it) }
            4 -> classificationProbability.classProbabilitiesCnnExFeat.let {
                CNNExFeatResultFragment(
                    it
                )
            }

            5 -> classificationProbability.classProbabilitiesYolo.let { YoloResultFragment(it) }
            else -> classificationProbability.classProbabilitiesXgb.let { XGBResultFragment(it) }
        }
    }
}