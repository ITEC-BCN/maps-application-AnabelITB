package com.example.mapsapp.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mapsapp.ui.navigation.Destination

enum class DrawerItem(
    val icon: ImageVector,
    val text: String,
    val route: Destination
) {
    Mapa(Icons.Default.LocationOn, "Mapa", Destination.Mapa),
    Marcadores(Icons.Default.Menu, "Marcadores", Destination.Marcadores)
}
