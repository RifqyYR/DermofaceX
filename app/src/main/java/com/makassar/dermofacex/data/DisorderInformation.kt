package com.makassar.dermofacex.data

import java.io.Serializable

data class DisorderInformation(
    val name: String,
    val definition: String,
    val cause: String,
    val treatment: List<String>,
    val prevention: String,
    val avoided_ingredients: List<String>,
) : Serializable
