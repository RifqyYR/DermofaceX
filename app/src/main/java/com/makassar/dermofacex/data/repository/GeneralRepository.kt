package com.makassar.dermofacex.data.repository

import android.util.Log
import com.makassar.dermofacex.data.Resource
import com.makassar.dermofacex.data.network.ApiService
import com.makassar.dermofacex.utils.parseErrorMessageFromHttpException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import retrofit2.HttpException

class GeneralRepository(private val apiService: ApiService) {
    fun classifyImage(
        image: MultipartBody.Part?
    ) = flow {
        emit(Resource.Loading())
        try {
            val result = apiService.classifyImage(image)
            emit(Resource.Success(result))
        } catch (e: HttpException) {
            val errorMessage = parseErrorMessageFromHttpException(e)
            emit(Resource.Error(errorMessage))
        } catch (e: Exception) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(Dispatchers.IO)
}