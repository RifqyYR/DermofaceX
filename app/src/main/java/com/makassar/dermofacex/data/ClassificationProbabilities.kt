package com.makassar.dermofacex.data

import com.makassar.dermofacex.data.response.ClassProbabilitiesCb
import com.makassar.dermofacex.data.response.ClassProbabilitiesCnn
import com.makassar.dermofacex.data.response.ClassProbabilitiesCnnExFeat
import com.makassar.dermofacex.data.response.ClassProbabilitiesLgb
import com.makassar.dermofacex.data.response.ClassProbabilitiesXgb
import com.makassar.dermofacex.data.response.ClassProbabilitiesYolo
import java.io.Serializable

data class ClassificationProbabilities(
    val classProbabilitiesXgb: ClassProbabilitiesXgb,
    val classProbabilitiesCb: ClassProbabilitiesCb,
    val classProbabilitiesCnn: ClassProbabilitiesCnn,
    val classProbabilitiesLgb: ClassProbabilitiesLgb,
    val classProbabilitiesYolo: ClassProbabilitiesYolo,
    val classProbabilitiesCnnExFeat: ClassProbabilitiesCnnExFeat
) : Serializable
