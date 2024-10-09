package com.makassar.dermofacex.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClassificationProbabilityResponse(

    @field:SerializedName("class_probabilities_cnn_ex_feat")
    val classProbabilitiesCnnExFeat: ClassProbabilitiesCnnExFeat? = null,

    @field:SerializedName("class_probabilities_xgb")
    val classProbabilitiesXgb: ClassProbabilitiesXgb? = null,

    @field:SerializedName("class_probabilities_cnn")
    val classProbabilitiesCnn: ClassProbabilitiesCnn? = null,

    @field:SerializedName("class_probabilities_cb")
    val classProbabilitiesCb: ClassProbabilitiesCb? = null,

    @field:SerializedName("class_probabilities_lgb")
    val classProbabilitiesLgb: ClassProbabilitiesLgb? = null,

    @field:SerializedName("class_probabilities_yolo")
    val classProbabilitiesYolo: ClassProbabilitiesYolo? = null
) : Parcelable

@Parcelize
data class ClassProbabilitiesYolo(

    @field:SerializedName("Hiperpigmentasi")
    val hiperpigmentasi: String? = null,

    @field:SerializedName("Jerawat")
    val jerawat: String? = null,

    @field:SerializedName("Kemerahan")
    val kemerahan: String? = null,

    @field:SerializedName("Normal")
    val normal: String? = null,

    @field:SerializedName("Berminyak")
    val berminyak: String? = null,

    @field:SerializedName("Komedo")
    val komedo: String? = null
) : Parcelable

@Parcelize
data class ClassProbabilitiesXgb(

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

@Parcelize
data class ClassProbabilitiesLgb(

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

@Parcelize
data class ClassProbabilitiesCnn(

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

@Parcelize
data class ClassProbabilitiesCb(

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

@Parcelize
data class ClassProbabilitiesCnnExFeat(

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
