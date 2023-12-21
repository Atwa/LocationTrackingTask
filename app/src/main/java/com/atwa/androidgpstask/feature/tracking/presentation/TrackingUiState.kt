package com.atwa.androidgpstask.feature.tracking.presentation

import android.location.Location

data class TrackingUiState(
    val location: Location? = null,
    val gpsEnabled: Boolean? = null,
    val timerRunning: Boolean? = null,
    val isLocationMocked: Boolean = false,
)