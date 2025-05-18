package com.example.mapsapp.ui.screens.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.utils.AuthState
import com.example.mapsapp.utils.SharedPreferencesHelper
import com.example.mapsapp.viewmodels.AuthViewModel
import com.example.mapsapp.viewmodels.AuthViewModelFactory

@Composable
fun Registre(navToDrawer: () -> Unit) {
    val context = LocalContext.current
    val viewModel: AuthViewModel =
        viewModel(factory = AuthViewModelFactory(SharedPreferencesHelper(context)))

    val authState by viewModel.authState.observeAsState()
    val email by viewModel.email.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val showError by viewModel.showError.observeAsState(false)
    if (authState == AuthState.Authenticated) {
        navToDrawer()
    } else {
        if (showError) {
            val errorMessage = (authState as AuthState.Error).message
            if (errorMessage!!.contains("weak_password")) {
                Toast.makeText(
                    context,
                    "Password should be at least 6 characters",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(context, "An error has ocurred", Toast.LENGTH_LONG).show()
            }
            viewModel.errorMessageShowed()
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Registro", fontSize = 20.sp)
        Spacer(Modifier.height(25.dp))
        TextField(
            value = email,
            onValueChange = { viewModel.editEmail(it) },
            placeholder = { Text("Introduce tu email") }
        )
        PasswordField(password) {
            viewModel.editPassword(it)
        }
        Spacer(Modifier.height(25.dp))
        Button(onClick = { viewModel.signUp() }) {
            Text("Iniciar sesión")
        }
    }
}