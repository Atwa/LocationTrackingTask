package com.atwa.androidgpstask.feature.tracking.data

import android.location.Location
import com.atwa.androidgpstask.core.network.NetworkRouter.invokeCall
import javax.inject.Inject

class TrackingRepository @Inject constructor(
    private val trackingApi: TrackingApi,
) {
    suspend fun uploadLocation(location: Location) = invokeCall {
        trackingApi.uploadLocation(
            LocationUpdateRequest(
                location.longitude,
                location.longitude
            )
        )
    }
}