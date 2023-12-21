package com.atwa.androidgpstask.feature.tracking.infrastructure

import android.location.Location

interface LocationTracker {
    fun fetchLocation(callback: LocationResponse?)
    fun setGpsProviderCallback(gpsCallback: GpsProviderCallback)
    fun stopFetch()
    fun resetAcquirement()
    fun isGpsProviderEnabled(): Boolean

    /**
     * @brief The location response for the callback of the LocationHelper
     */
    abstract class LocationResponse {
        /**
         * Called when the request was initiated
         */
        fun onRequest() {}

        /**
         * Called when the location changes
         *
         * @param location
         * The new location
         */
        open fun onLocationChanged(location: Location?) {}

        /**
         * Called when the request failed
         *
         * @param message
         * The message
         * @param messageId
         * The ID of the message
         */
        open fun onLocationFailed(message: String?, messageId: Int) {}
    }

    interface GpsProviderCallback {
        /**
         * Called when Gps is disabled
         */
        open fun onGpsDisabled() {}

        /**
         * Called when Gps is enabled
         */
        open fun onGpsEnabled() {}
    }

}