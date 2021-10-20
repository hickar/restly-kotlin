package com.hickar.restly.ui.responseBody

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hickar.restly.databinding.ResponseBodyRawBinding
import com.hickar.restly.ui.request.RequestViewModel

class ResponseBodyRawFragment : Fragment() {
    private var _binding: ResponseBodyRawBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RequestViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ResponseBodyRawBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.response.observe(viewLifecycleOwner) { response ->
            binding.responseBodyRawData.text = response.body?.string()
        }
    }
}