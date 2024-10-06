package com.makassar.dermofacex.ui.fragments.detailtab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.makassar.dermofacex.databinding.FragmentDetailTabBinding

class PreventionFragment(private val content: String) : Fragment() {
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

        binding.tvDetail.text = content
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}