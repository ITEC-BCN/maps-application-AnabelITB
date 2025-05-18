package com.example.mapsapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.model.Marcador
import com.example.mapsapp.viewmodels.MyViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import androidx.compose.runtime.getValue
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(modifier: Modifier = Modifier, navToMarker: (Double, Double) -> Unit) {
    var myViewModel: MyViewModel = viewModel()
    val listaMarcadores by myViewModel.listaMarcadores.observeAsState()
    myViewModel.getAllMarcadors()



    Column(modifier.fillMaxSize()) {
        val itb = LatLng(41.4534225, 2.1837151)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(itb, 17f)
        }
        GoogleMap(
            modifier
                .fillMaxSize()
                .padding(bottom = 45.dp),
            cameraPositionState = cameraPositionState,
            onMapLongClick = {
                navToMarker(it.latitude, it.longitude)
            }) {

            listaMarcadores?.forEach {
                val coordenadas = LatLng(it.latitud, it.longitud)
                Marker(
                    state = MarkerState(position = coordenadas), title = it.name,
                    snippet = it.descripcion
                )
            }
        }
    }
}
