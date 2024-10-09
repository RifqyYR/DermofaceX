package com.makassar.dermofacex.data.network

import com.makassar.dermofacex.data.response.ClassificationProbabilityResponse
import com.makassar.dermofacex.data.response.ClassificationResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("upload/")
    suspend fun classifyImage(
        @Part image: MultipartBody.Part?,
    ): ClassificationResponse

    @Multipart
    @POST("upload/")
    suspend fun getProbability(
        @Part image: MultipartBody.Part?,
    ): ClassificationProbabilityResponse
}