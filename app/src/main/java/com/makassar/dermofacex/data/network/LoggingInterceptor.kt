package com.makassar.dermofacex.data.network

import android.content.ContentValues.TAG
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().addHeader("Accept", "application/json").build()
        val response = chain.proceed(request)
        Log.d(TAG, "intercept: ${response.request}")
        Log.d(TAG, "intercept1: ${response.code}")
        Log.d(TAG, "intercept2: ${response.networkResponse}")
        Log.d(TAG, "intercept3: ${response.isSuccessful}")
        Log.d(TAG, "intercept4: ${response.headers}")
        Log.d(TAG, "intercept5: ${response.isRedirect}")
        Log.d(TAG, "intercept6: ${response.request}")

        if (response.code == 400) {
            val errorBody = response.body?.string()
            val errorMessage = parseErrorMessageFromJson(errorBody)

            // Create a new response with the error message
            return response.newBuilder()
                .body(errorBody?.toResponseBody(response.body?.contentType()))
                .message(errorMessage ?: "Bad Request")
                .build()
        } else if (response.code == 401) {
            val errorBody = response.body?.string()
            val errorMessage = parseErrorMessageFromJson(errorBody)

            // Create a new response with the error message
            return response.newBuilder()
                .body(errorBody?.toResponseBody(response.body?.contentType()))
                .message(errorMessage ?: "Bad Request")
                .build()
        }

        return response
    }

    private fun parseErrorMessageFromJson(errorBody: String?): String? {
        if (errorBody.isNullOrEmpty()) {
            return null
        }

        try {
            val gson = Gson()
            val jsonObject = gson.fromJson(errorBody, JsonObject::class.java)

            // Assuming the error message is stored in a field called "message"
            val errorMessage = jsonObject.get("message")?.asString
            if (!errorMessage.isNullOrEmpty()) {
                return errorMessage
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}