package com.example.mapsapp.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.viewmodels.MyViewModel
import java.io.File
import androidx.compose.runtime.getValue
import coil.compose.rememberAsyncImagePainter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailMarkerScreen(modifier: Modifier, id: Int, navigateBack: () -> Unit) {
    var viewModel: MyViewModel = viewModel()
    viewModel.getStudent(id)
    val title by viewModel.markerName.observeAsState("")
    val description by viewModel.markerDescript.observeAsState("")
    val imagen by viewModel.markerImage.observeAsState("")
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && imageUri != null) {
                val stream = context.contentResolver.openInputStream(imageUri!!)
                bitmap = BitmapFactory.decodeStream(stream)
            }
        }
    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = it
                val stream = context.contentResolver.openInputStream(it)
                bitmap = BitmapFactory.decodeStream(stream)
            }
        }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { viewModel.editMarkerName(it) },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { viewModel.editMarkerDescript(it) },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Selecciona una opción") },
                text = { Text("¿Quieres tomar una foto o elegir una desde la galería?") },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        val uri = createImageUri(context)
                        imageUri = uri
                        takePictureLauncher.launch(uri!!)
                    }) {
                        Text("Tomar Foto")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                        pickImageLauncher.launch("image/*")
                    }) {
                        Text("Elegir de Galería")
                    }
                }
            )
        }
        Button(onClick = { showDialog = true }) {
            Text("Abrir Cámara o Galería")
        }
        Spacer(modifier = Modifier.height(24.dp))

        if (bitmap != null) {
            Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = rememberAsyncImagePainter(model = imagen),
                contentDescription = title,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            viewModel.updateMarker(id, title, description, bitmap)
            navigateBack()
        }) {
            Text("Guardar Marcador")
        }
    }
}
