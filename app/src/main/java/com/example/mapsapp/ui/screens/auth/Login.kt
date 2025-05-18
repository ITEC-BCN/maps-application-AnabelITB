package com.example.mapsapp.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.utils.SharedPreferencesHelper
import com.example.mapsapp.viewmodels.AuthViewModel
import com.example.mapsapp.viewmodels.AuthViewModelFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mapsapp.utils.AuthState

@Composable
fun Login(navToRegister: () -> Unit, navToDrawer: () -> Unit) {
    val context = LocalContext.current
    val myViewModel: AuthViewModel =
        viewModel(factory = AuthViewModelFactory(SharedPreferencesHelper(context)))
    val authState by myViewModel.authState.observeAsState()
    val email by myViewModel.email.observeAsState("")
    val password by myViewModel.password.observeAsState("")
    val showError by myViewModel.showError.observeAsState(false)

    if (authState == AuthState.Authenticated) {
        navToDrawer()
    } else {
        if (showError) {
            val errorMessage = (authState as AuthState.Error).message
            if (errorMessage!!.contains("invalid_credentials")) {
                Toast.makeText(context, "Invalid credentials", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "An error has ocurred", Toast.LENGTH_LONG).show()
            }
            myViewModel.errorMessageShowed()
        }

    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Iniciar Sessión", fontSize = 20.sp)
        Spacer(Modifier.height(25.dp))
        TextField(
            value = email,
            onValueChange = { myViewModel.editEmail(it) },
            placeholder = { Text("Introduce tu email") }
        )
        PasswordField(password) { myViewModel.editPassword(it) }
        Spacer(Modifier.height(25.dp))
        Button(onClick = { myViewModel.signIn() }) {
            Text("Iniciar sesión")
        }
        Spacer(Modifier.height(25.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("No tienes una cuenta?")
            TextButton(onClick = {
                navToRegister()
            }) { Text("Registrate aquí") }
        }
    }
}

@Composable
fun PasswordField(password: String, onValueChange: (String) -> Unit) {

    var passwordVisibility by remember { mutableStateOf(false) }
    TextField(
        value = password,
        onValueChange = { onValueChange(it) },
        label = { Text("Enter your password") },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisibility) {
                Icons.Filled.Close
            } else {
                Icons.Filled.Search
            }
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(imageVector = image, contentDescription = "Password visibility")
            }
        },
        visualTransformation = if (passwordVisibility) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }
    )
}