package com.example.mapsapp.model

import kotlinx.serialization.Serializable

@Serializable
data class Marcador(
    val id : Int = 0,
    val name: String,
    val descripcion: String,
    val image: String,
    val latitud: Double,
    val longitud: Double
)
