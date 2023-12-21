package com.atwa.androidgpstask.feature.tracking.infrastructure

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import javax.inject.Inject

/**
 * @brief This class is used to fetch the user's current location using either
 * cached location (if available) or requests it using the
 * LocationListener if not
 */
class LocationTracker @Inject constructor(private val mContext: Context) : LocationListener {
    private val mLocationManager: LocationManager =
        mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var mCallback: LocationResponse? = null
    private var mGpsCallback: GpsProviderCallback? = null
    private var mAccuracy = Accuracy.FINE
    private var location: Location? = null
    private var locationAcquired = false


    /**
     * Gets the current set desired accuracy for a fetch
     *
     * @return The accuracy in meters
     */
    /**
     * Sets the desired accuracy for the fetch
     *
     * @param accuracy
     * The new accuracy
     */
    var accuracy = 30.0f

    /**
     * Determines the accuracy of the fetch
     */
    enum class Accuracy {
        /**
         * Get the location as close to the real point as possible
         */
        FINE,

        /**
         * Get the location by any means
         */
        COARSE
    }

    /**
     * Cancels the request
     */
    fun cancelRequest() {
        mLocationManager.removeUpdates(this)
        if (mCallback != null) {
            mCallback?.onLocationFailed("Canceld", MESSAGE_FORCED_CANCEL)
        }
    }

    /**
     * Fetches the location using Fine accuracy. Note: if the response returns
     * location fetch failed, use the helper to get the cached location, then
     * finally fail if that is null
     *
     * @param callback
     * The callback for the request
     */
    fun fetchLocation(callback: LocationResponse?) {
        fetchLocation(Accuracy.FINE, callback)
    }

    fun isGpsProviderEnabled(): Boolean {
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun setGpsProviderCallback(gpsCallback:GpsProviderCallback){
        mGpsCallback = gpsCallback
    }

    /**
     * Fetches the location
     *
     * @param timeout
     * The time out for the request in MS
     * @param accuracy
     * The accuracy of the fetch
     * @param callback
     * The callback for the request
     */
    @SuppressLint("MissingPermission")
    fun fetchLocation(accuracy: Accuracy, callback: LocationResponse?) {
        mCallback = callback
        mCallback?.onRequest()
        mAccuracy = accuracy
        var userLocation: Location? = null

        // Try to get the cache location first
        userLocation = cachedLocation
        // if (userLocation == null)
        run {
            try {
                mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0f,
                    this
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0,
                    0f,
                    this
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                mLocationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER,
                    0,
                    0f,
                    this
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Gets the cached location
     *
     * @return The location, or null if one was not retrieved
     */
    private val cachedLocation: Location?
        @SuppressLint("MissingPermission")
        get() {
            val providers = mLocationManager.getProviders(true)
            for (i in providers.indices.reversed()) {
                val loc = mLocationManager.getLastKnownLocation(providers[i])
                if ((loc?.accuracy ?: 0f) < (location?.accuracy ?: 0f)) {
                    location = loc
                }
            }
            return location
        }


    override fun onLocationChanged(location: Location) {
        if (!locationAcquired) {
            mCallback?.onLocationChanged(location)
            locationAcquired = true
        }
    }

    fun resetAcquirement() {
        locationAcquired = false
    }

    fun stopFetch() {
        mLocationManager.removeUpdates(this)
    }


    override fun onProviderDisabled(provider: String) {
        if (provider == LocationManager.GPS_PROVIDER)
            mGpsCallback?.onGpsDisabled()
        val providers = mLocationManager.getProviders(true)
        var allOn = false
        for (i in providers.indices.reversed()) {
            allOn = allOn or Settings.Secure.isLocationProviderEnabled(
                mContext.contentResolver,
                providers[i]
            )
        }
        if (!allOn) {
            mLocationManager.removeUpdates(this)
            if (mCallback != null) {
                mCallback?.onLocationFailed("All providers disabled", MESSAGE_PROVIDER_DISABLED)
            }
        }
    }

    override fun onProviderEnabled(provider: String) {
        if (provider == LocationManager.GPS_PROVIDER)
            mGpsCallback?.onGpsEnabled()
        fetchLocation(Accuracy.FINE, mCallback)
    }

    @Deprecated("Deprecated in Java")
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
    }

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

    interface GpsProviderCallback{
        open fun onGpsDisabled() {}
        open fun onGpsEnabled() {}
    }

    companion object {
        /**
         * Message ID: Used when a provider has been disabled
         */
        const val MESSAGE_PROVIDER_DISABLED = 0

        /**
         * Message ID: Used when the search has timed out
         */
        const val MESSAGE_TIMEOUT = 1

        /**
         * Message ID: Used when the user cancels the request
         */
        const val MESSAGE_FORCED_CANCEL = 2
    }
}