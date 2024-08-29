package com.makassar.dermofacex.utils

import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException

fun parseErrorMessageFromHttpException(e: HttpException): String {
    val response = e.response()
    val errorBody: ResponseBody? = response?.errorBody()

    return try {
        val errorJson = errorBody?.string()
        val jsonObject = Gson().fromJson(errorJson, JsonObject::class.java)
        val errorMessage = jsonObject.get("message")?.asString
        errorMessage ?: "Unknown Error"
    } catch (e: IOException) {
        "Unknown Error"
    }
}