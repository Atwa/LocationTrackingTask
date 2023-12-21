package com.atwa.androidgpstask.feature.tracking.presentation

import android.location.Location
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atwa.androidgpstask.feature.tracking.data.TrackingRepository
import com.atwa.androidgpstask.feature.tracking.infrastructure.LocationTracker
import com.atwa.androidgpstask.feature.tracking.infrastructure.MockLocationDetector
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(
    private val locationTracker: LocationTracker,
    private val locationMockDetector: MockLocationDetector,
    private val trackingRepository: TrackingRepository,
) : ViewModel() {

    val TIME = 5000L
    val TAG = this.javaClass.simpleName

    private var isTimerRunning = false
    var timeUntilFinish = TIME
    private lateinit var locationTimer: CountDownTimer

    private val _uiState = MutableStateFlow(TrackingUiState())
    val uiState = _uiState.asStateFlow()

    private fun startTimer() {
        isTimerRunning = true
        locationTimer = object : CountDownTimer(timeUntilFinish, 1000L) {
            override fun onTick(millisUntilFinish: Long) {
                timeUntilFinish = millisUntilFinish
            }

            override fun onFinish() {
                timeUntilFinish = TIME
                locationTracker.resetAcquirement()
                checkGpsStatus()
                startTimer()
            }
        }.start()
    }

    fun setupTracker(){
        startTimer()
        listenForGpsUpdates()
    }

    fun fetchLocation() {
        locationTracker.fetchLocation(callback = object : LocationTracker.LocationResponse() {
            override fun onLocationChanged(location: Location?) {
                location?.let {
                    Log.d(TAG, "Location update Received : $it")
                    if (areLocationsDistinct(uiState.value.location, location))
                        uploadLatestLocation(location)
                    _uiState.update { state ->
                        state.copy(
                            location = location,
                            gpsEnabled = locationTracker.isGpsProviderEnabled(),
                            timerRunning = isTimerRunning,
                            isLocationMocked = isLocationMocked(location)
                        )
                    }
                }
            }
        })
    }

    private fun isLocationMocked(location: Location) = locationMockDetector.isMockLocation(location)

    private fun checkGpsStatus() {
        val hasGpsBeenEnabled = _uiState.value.gpsEnabled
        val isGpsEnabled = locationTracker.isGpsProviderEnabled()
        if (hasGpsBeenEnabled != isGpsEnabled) {
            _uiState.update { it.copy(gpsEnabled = isGpsEnabled) }
            fetchLocation()
        }
    }

    private fun listenForGpsUpdates() {
        locationTracker.setGpsProviderCallback(object : LocationTracker.GpsProviderCallback {
            override fun onGpsEnabled() {
                _uiState.update { it.copy(gpsEnabled = true) }
            }

            override fun onGpsDisabled() {
                _uiState.update { it.copy(gpsEnabled = false) }
            }
        })
    }

    private fun uploadLatestLocation(location: Location) {
        viewModelScope.launch(Dispatchers.IO) {
            trackingRepository.uploadLocation(location).fold({
                Log.d(TAG, "Location upload Succeeded : $it")
            }, {
                Log.d(TAG, "Location upload Failed : ${it.message}")
            })
        }
    }

    private fun areLocationsDistinct(
        prevLocation: Location?,
        currentLocation: Location,
    ): Boolean {
        return (prevLocation?.latitude != currentLocation.latitude || prevLocation.longitude != currentLocation.longitude)
    }

    fun updateTimerStatus() {
        if (isTimerRunning) stopTimer()
        else startTimer()
        _uiState.update { it.copy(timerRunning = isTimerRunning) }
    }

    private fun stopTimer() {
        isTimerRunning = false
        locationTimer.cancel()
    }

    override fun onCleared() {
        locationTracker.stopFetch()
        locationTimer.cancel()
    }
}