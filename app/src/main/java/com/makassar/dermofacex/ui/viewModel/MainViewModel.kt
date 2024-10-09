package com.makassar.dermofacex.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makassar.dermofacex.data.Resource
import com.makassar.dermofacex.data.repository.GeneralRepository
import com.makassar.dermofacex.data.response.ClassificationProbabilityResponse
import com.makassar.dermofacex.data.response.ClassificationResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class MainViewModel(
    private val repository: GeneralRepository
) : ViewModel() {
    var image: MultipartBody.Part? = null

    private var _classify: MutableStateFlow<Resource<out ClassificationResponse>> =
        MutableStateFlow(Resource.Empty())
    val classify: StateFlow<Resource<out ClassificationResponse>> get() = _classify

    private var _probability: MutableStateFlow<Resource<out ClassificationProbabilityResponse>> =
        MutableStateFlow(Resource.Empty())
    val probability: StateFlow<Resource<out ClassificationProbabilityResponse>> get() = _probability

    fun getClassifyResult(
        image: MultipartBody.Part?
    ) = viewModelScope.launch {
        val response = repository.classifyImage(image)
        response.collectLatest {
            _classify.emit(it)
        }
    }

    fun getProbabilityResult(
        image: MultipartBody.Part?
    ) = viewModelScope.launch {
        val response = repository.getProbability(image)
        response.collectLatest {
            _probability.emit(it)
        }
    }

    fun resetClassifyState() {
        _classify.value = Resource.Empty()
    }

    fun resetProbabilityState() {
        _probability.value = Resource.Empty()
    }
}