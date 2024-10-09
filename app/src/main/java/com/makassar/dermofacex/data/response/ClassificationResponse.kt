package com.makassar.dermofacex.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClassificationResponse(

    @field:SerializedName("result")
    val result: String? = null,

    @field:SerializedName("class_probabilities")
    val classProbabilities: ClassProbabilities? = null,

    @field:SerializedName("probability")
    val probability: String? = null
) : Parcelable

@Parcelize
data class ClassProbabilities(

    @field:SerializedName("Hiperpigmentasi")
    val hiperpigmentasi: String? = null,

    @field:SerializedName("Jerawat")
    val jerawat: String? = null,

    @field:SerializedName("Normal")
    val normal: String? = null,

    @field:SerializedName("Kemerahan")
    val kemerahan: String? = null,

    @field:SerializedName("Komedo")
    val komedo: String? = null,

    @field:SerializedName("Berminyak")
    val berminyak: String? = null
) : Parcelable
