package com.atwa.androidgpstask.feature.tracking.data

import retrofit2.http.Body
import retrofit2.http.POST

interface TrackingApi {
    @POST("LocationTest")
    suspend fun uploadLocation(@Body location: LocationRequest): Any
}