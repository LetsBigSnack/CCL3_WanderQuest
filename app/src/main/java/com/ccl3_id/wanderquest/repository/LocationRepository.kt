package com.ccl3_id.wanderquest.repository

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.ccl3_id.wanderquest.services.LocationTrackingService
import kotlinx.coroutines.flow.asStateFlow

class LocationRepository(context: Context) {
    private var locationService: LocationTrackingService? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as LocationTrackingService.LocalBinder
            locationService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            locationService = null
        }
    }

    init {
        val intent = Intent(context, LocationTrackingService::class.java)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    fun getDistanceWalked(): Float {
        return locationService?.getDistanceWalked() ?: 0f
    }

    // Call this method from your Activity or Fragment's onDestroy to avoid memory leaks
    fun unbindService(context: Context) {
        if (locationService != null) {
            context.unbindService(serviceConnection)
        }
    }

    fun resetDistance() {
        locationService?.resetDistance()
    }

}