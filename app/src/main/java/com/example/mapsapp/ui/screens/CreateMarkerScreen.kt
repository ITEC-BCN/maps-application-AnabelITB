package com.example.mapsapp.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.core.graphics.scale
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.mapsapp.viewmodels.MyViewModel
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateMarkerScreen(
    modifier: Modifier,
    latitud: Double,
    longitud: Double,
    navigateBack: () -> Unit
) {
    var viewModel: MyViewModel = viewModel()
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = it
                val stream = context.contentResolver.openInputStream(it)
                bitmap = BitmapFactory.decodeStream(stream)
            }
        }
    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && imageUri != null) {
                val stream = context.contentResolver.openInputStream(imageUri!!)
                stream?.use { // Decodificar el flujo a un Bitmap
                    val originalBitmap = BitmapFactory.decodeStream(it)

                    // Obtener las dimensiones originales de la imagen
                    val originalWidth = originalBitmap.width
                    val originalHeight = originalBitmap.height
                    // Definir el aspect ratio (relación entre ancho y alto)
                    val aspectRatio = originalWidth.toFloat() / originalHeight.toFloat()

                    // Establecer el tamaño máximo que deseas para la imagen (por ejemplo, un ancho máximo)
                    val maxWidth = 800
                    // Puedes establecer el valor que prefieras
                    // Calcular el nuevo ancho y alto manteniendo el aspect ratio
                    val newWidth = maxWidth
                    val newHeight = (newWidth / aspectRatio).toInt()

                    // Redimensionar el bitmap mientras se mantiene el aspect ratio
                    val resizedBitmap = originalBitmap.scale(newWidth, newHeight)

                    // Establecer el Bitmap redimensionado en el ViewModel
//                    viewModel.setImagenBitMap(resizedBitmap)
                    bitmap = resizedBitmap
                } ?: run {
                    Log.e("TakePicture", "Error al abrir InputStream para la URI de la imagen.")
                }
            } else {
                Log.e("TakePicture", "La imagen no fue tomada o la URI de la imagen es nula.")
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
            onValueChange = { title = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
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

        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            viewModel.insertNewMarker(title, description, bitmap, latitud, longitud)
            navigateBack()
        }) {
            Text("Guardar Marcador")
        }
    }
}

fun createImageUri(context: Context): Uri? {
    val file = File.createTempFile("temp_image_", ".jpg", context.cacheDir).apply {
        createNewFile()
        deleteOnExit()
    }
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}
