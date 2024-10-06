package com.makassar.dermofacex.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException

fun parseErrorMessageFromHttpException(e: HttpException): String {
    val response = e.response()
    val errorBody: ResponseBody? = response?.errorBody()

    return try {
        val contentType = e.response()?.headers()?.get("Content-Type")
        if (contentType != null && contentType.contains("text/html", ignoreCase = true)) {
            "The server returned an HTML error page."
        } else {
            val errorJson = errorBody?.string()
            val jsonObject = Gson().fromJson(errorJson, JsonObject::class.java)
            val errorMessage = jsonObject.get("message")?.asString
            errorMessage ?: "Unknown Error"
        }
    } catch (e: IOException) {
        "Unknown Error"
    }
}

fun getFileFromUri(context: Context, uri: Uri): File {
    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
    cursor?.moveToFirst()
    val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
    val filePath = cursor?.getString(columnIndex ?: 0)
    cursor?.close()
    return File(filePath ?: "")
}