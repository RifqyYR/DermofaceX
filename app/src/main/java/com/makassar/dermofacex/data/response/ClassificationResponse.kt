package com.makassar.dermofacex.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClassificationResponse(
    @field:SerializedName("result")
    val result: String? = null
) : Parcelable

