package com.atwa.androidgpstask.feature.tracking.presentation

import android.location.Location
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atwa.androidgpstask.designsystem.widget.LoadingScreen

@Composable
fun TrackingScreen(viewModel: TrackingViewModel) {
    val state = viewModel.uiState.collectAsState().value
    LaunchedEffect(key1 = 1) {
        viewModel.setupTracker()
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        state.location?.let { TrackingStatus(it) } ?: LoadingScreen()
        state.gpsEnabled?.let { GpsStatus(it) }
        LocationMocked(state.isLocationMocked)
        state.timerRunning?.let { TimerButton(it) { viewModel.updateTimerStatus() } }
    }
}

@Composable
fun TrackingStatus(location: Location) {
    Text(
        buildAnnotatedString {
            append("Location is : ")
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            ) {
                append(
                    "%,.5f".format(location.latitude) +
                            "," +
                            "%,.5f".format(location.longitude)
                )
            }
        }
    )
}

@Composable
fun GpsStatus(gpsEnabled: Boolean) {
    val status = if (gpsEnabled) "Enabled" else "Disabled"
    val statusColor = if (gpsEnabled) Color.Green else Color.Red
    Text(
        buildAnnotatedString {
            append("Gps is : ")
            withStyle(
                style = SpanStyle(
                    color = statusColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            ) {
                append(status)
            }
        },
        modifier = Modifier.padding(top = 20.dp)
    )
}


@Composable
fun LocationMocked(isLocationMocked: Boolean) {
    val status = if (isLocationMocked) "Mocked" else "Authentic"
    val statusColor = if (isLocationMocked) Color.Red else Color.Green
    Text(
        buildAnnotatedString {
            append("Location is : ")
            withStyle(
                style = SpanStyle(
                    color = statusColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            ) {
                append(status)
            }
        },
        modifier = Modifier.padding(top = 20.dp)
    )
}

@Composable
fun TimerButton(isRunning: Boolean, onClick: () -> Unit) {
    Button(
        modifier = Modifier.padding(top = 20.dp),
        onClick = { onClick() }
    ) {
        Text(
            text = "${if (isRunning) "Pause" else "Resume"} timer",
            fontWeight = FontWeight.Bold
        )
    }
}
