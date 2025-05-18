package com.example.mapsapp

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.example.mapsapp.ui.navigation.MainNavigationWrapper
import com.example.mapsapp.ui.theme.MapsAppTheme

class MainActivity : ComponentActivity() {

    //VARIABLES PARA LA MÚSICA
    private var mediaPlayer: MediaPlayer? = null
    private var currentPosition: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //LAS 3 SIGUIENTES LINIAS SON PARA LA MÚSICA
        mediaPlayer = MediaPlayer.create(this, R.raw.musikitapaldanidani)
        mediaPlayer?.isLooping = true // Reproduce la canción en bucle
        mediaPlayer?.start()


        enableEdgeToEdge()
        setContent {
            MapsAppTheme {
                MainNavigationWrapper()
            }
        }
    }



    //DE AQUÍ PARA ABAJO ES SOLO LA MÚSICA

    override fun onResume() {
        super.onResume()
        // Reanuda la música desde la última posición guardada
        mediaPlayer?.apply {
            seekTo(currentPosition)  // Vuelve a la posición guardada
            start()  // Reanuda la reproducción
        }
    }

    override fun onPause() {
        super.onPause()
        // Guarda la posición actual y pausa la música
        mediaPlayer?.let {
            currentPosition = it.currentPosition  // Guarda la posición actual
            it.pause()  // Pausa la música
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Libera los recursos del MediaPlayer cuando la actividad se destruye
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
