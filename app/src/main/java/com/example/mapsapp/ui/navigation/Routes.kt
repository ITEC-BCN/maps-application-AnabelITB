package com.example.mapsapp.ui.navigation

import kotlinx.serialization.Serializable


sealed class Destination {

    @Serializable
    object Permissions : Destination()

    @Serializable
    object IniciarSesion : Destination()

    @Serializable
    object Register : Destination()

    @Serializable
    object Drawer : Destination()

    @Serializable
    object Mapa : Destination()

    @Serializable
    object Marcadores : Destination()

    @Serializable
    data class MarkerCreation(val latitud: Double, val longitud: Double)

    @Serializable
    data class MarkerDetail(val id: Int)
}