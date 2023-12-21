package com.atwa.androidgpstask.feature.tracking.infrastructure

import android.app.AppOpsManager
import android.content.Context
import android.location.Location
import android.os.Build
import android.os.Process
import android.provider.Settings
import com.atwa.androidgpstask.BuildConfig
import javax.inject.Inject


class LocationMockDetector @Inject constructor(val context: Context) {

    private fun isMockLocationEnabled(): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val opsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
                opsManager.checkOp(
                    AppOpsManager.OPSTR_MOCK_LOCATION,
                    Process.myUid(),
                    BuildConfig.APPLICATION_ID
                ) == AppOpsManager.MODE_ALLOWED
            } else {
                !Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ALLOW_MOCK_LOCATION
                ).equals("0")
            }
        } catch (e: Exception) {
            false
        }
    }

    fun isMockLocation(location: Location): Boolean {
        val isMockLocation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            location.isMock
        } else {
            location.isFromMockProvider
        }
        val isMockEnabled = isMockLocationEnabled()
        return isMockLocation || isMockEnabled
    }

}