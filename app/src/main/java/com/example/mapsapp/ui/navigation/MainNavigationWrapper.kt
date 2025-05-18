package com.example.mapsapp.ui.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.ui.navigation.Destination.*
import com.example.mapsapp.ui.screens.DrawerScreen
import com.example.mapsapp.ui.screens.PermissionsScreen
import com.example.mapsapp.ui.screens.auth.Login
import com.example.mapsapp.ui.screens.auth.Registre

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigationWrapper() {
    val navController = rememberNavController()
    NavHost(navController, Permissions) {

        composable<Permissions> {
            PermissionsScreen() {
                navController.navigate(IniciarSesion)
            }
        }

        //LOGIN
        composable<IniciarSesion> {
            Login({ navController.navigate(Register) }, { navController.navigate(Drawer) })
        }
        //Register
        composable<Register> {
            Registre() {
                navController.navigate(Drawer)
            }
        }

        composable<Drawer> {
            DrawerScreen {
                navController.navigate(IniciarSesion) {
                    popUpTo<IniciarSesion> { inclusive = true }
                }
            }
        }
    }
}
