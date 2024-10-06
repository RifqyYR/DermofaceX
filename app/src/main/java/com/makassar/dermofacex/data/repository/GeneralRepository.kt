package com.makassar.dermofacex.data.repository

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
            // Periksa kode status HTTP
            val errorMessage = when (e.code()) {
                404 -> "Resource not found (404). The server could not find the requested resource."
                500 -> "Internal Server Error (500). Something went wrong on the server."
                else -> parseErrorMessageFromHttpException(e) // Metode untuk parsing error dari JSON atau lainnya
            }
            emit(Resource.Error(errorMessage))
        } catch (e: Exception) {
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(Dispatchers.IO)
}