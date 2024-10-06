package com.makassar.dermofacex.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.makassar.dermofacex.ui.fragments.detailtab.AvoidedIngredientsTab
import com.makassar.dermofacex.ui.fragments.detailtab.CauseFragment
import com.makassar.dermofacex.ui.fragments.detailtab.TreatmentFragment

class DetailPagerAdapter(
    fragment: Fragment,
    private val cause: String,
    private val treatment: List<String>,
    private val avoided_ingredients: List<String>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CauseFragment(cause)
            1 -> TreatmentFragment(treatment)
            2 -> AvoidedIngredientsTab(avoided_ingredients)
            else -> CauseFragment(cause)
        }
    }
}