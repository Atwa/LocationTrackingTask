package com.atwa.androidgpstask.feature.tracking.infrastructure

import android.location.Location

interface MockLocationDetector {
    fun isMockLocation(location: Location): Boolean
}