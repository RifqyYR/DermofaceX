package com.makassar.dermofacex.ui.fragments.detailtab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.makassar.dermofacex.databinding.FragmentDetailTabBinding

class TreatmentFragment(private val content: List<String>) : Fragment() {
    private var _binding: FragmentDetailTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val formattedTreatment = content.joinToString(separator = "\n") { "• $it" }
        binding.tvDetail.text = formattedTreatment
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}