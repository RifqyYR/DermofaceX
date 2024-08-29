package com.makassar.dermofacex.ui.viewModel

import com.makassar.dermofacex.data.Resource
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import com.makassar.dermofacex.data.repository.GeneralRepository
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
        MutableStateFlow(Resource.Loading())
    val classify: StateFlow<Resource<out ClassificationResponse>> get() = _classify

    fun getClassifyResult(
        image: MultipartBody.Part?
    ) = viewModelScope.launch {
        val response = repository.classifyImage(image)
        response.collectLatest {
            _classify.emit(it)
        }
    }
}