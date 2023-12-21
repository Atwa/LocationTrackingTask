package com.atwa.androidgpstask.feature.tracking.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.atwa.androidgpstask.designsystem.theme.AndroidGpsTaskTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackingActivity : ComponentActivity() {

    private val viewModel by viewModels<TrackingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidGpsTaskTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LocationPermission()
                }
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun LocationPermission() {
        val locationPermissionsState = rememberMultiplePermissionsState(
            listOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )
        if (locationPermissionsState.allPermissionsGranted) {
            TrackingScreen(viewModel)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (locationPermissionsState.shouldShowRationale) {
                    ShowLocationPermissionRationale()
                } else {
                    RequestLocationScreen { locationPermissionsState.launchMultiplePermissionRequest() }
                }
            }
        }
    }

    @Composable
    fun RequestLocationScreen(requestPermissions: () -> Unit) {
        Text(text = "Location permission is required for the app, Please enable location permission.")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = requestPermissions) {
            Text("Enable location")
        }
    }

    @Composable
    fun ShowLocationPermissionRationale() {
        val context = LocalContext.current
        AlertDialog(
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            ),
            onDismissRequest = {},
            title = {
                Text("Permission Required")
            },
            text = {
                Text("You need to approve this permission so that app can function properly...")
            },
            confirmButton = {
                TextButton(onClick = {
                    openApplicationSettings()
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    makeText(
                        context,
                        "App can't function without Location permission",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    Text("Deny")
                }
            })
    }

    private fun openApplicationSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        ).also {
            startActivity(it)
        }
    }

}

