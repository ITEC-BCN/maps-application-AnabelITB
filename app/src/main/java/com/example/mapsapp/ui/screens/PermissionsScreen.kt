package com.example.mapsapp.ui.screens

import android.content.pm.PermissionInfo
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import com.example.mapsapp.Manifest
import com.example.mapsapp.utils.PermissionStatus

@Composable
fun PermissionsScreen(){
    val activity = LocalActivity.current
    val viewModel = viewModel<PermissionViewModel>()
    val permissionStatus = viewModel.permissionStatus.value
    var alreadyRequested by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        val result = when {
            granted -> PermissionStatus.Granted
            ActivityCompat.shouldShowRequestPermissionRationale(activity!!,Manifest.permission.ACCESS_FINE_LOCATION)
                -> PermissionStatus.Denied
            else -> PermissionStatus.PermanentlyDenied
        }
        viewModel.updatePermissionStatus(result)
    }
}