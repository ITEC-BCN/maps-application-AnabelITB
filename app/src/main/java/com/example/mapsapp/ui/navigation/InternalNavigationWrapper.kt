package com.example.mapsapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mapsapp.ui.navigation.Destination.*
import com.example.mapsapp.ui.screens.CreateMarkerScreen
import com.example.mapsapp.ui.screens.DetailMarkerScreen
import com.example.mapsapp.ui.screens.MapScreen
import com.example.mapsapp.ui.screens.MarkerListScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InternalNavigationWrapper(navController: NavHostController, modifier: Modifier){
    NavHost(navController, Mapa){
        composable<Mapa> {
            MapScreen(){ latitud, longitud ->
                navController.navigate(MarkerCreation(latitud, longitud))
            }
        }
        composable<Marcadores> {
            MarkerListScreen(modifier){ id->
                navController.navigate(MarkerDetail(id))
            }
        }
        composable<MarkerCreation> { backStrackEntry ->
            val coordenadas = backStrackEntry.toRoute<MarkerCreation>()
            CreateMarkerScreen(modifier,coordenadas.latitud, coordenadas.longitud){
                navController.popBackStack()
            }
        }
        composable<MarkerDetail> { backStrackEntry ->
            val id =  backStrackEntry.toRoute<MarkerDetail>()
            DetailMarkerScreen(modifier,id.id) {
                navController.popBackStack()
            }

        }
    }
}