package com.ccl3_id.wanderquest.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Binder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.ccl3_id.wanderquest.R

class LocationTrackingService : Service(), LocationListener {
    private lateinit var locationManager: LocationManager
    private var lastLocation: Location? = null
    private var distanceWalked: Float = 0f
    private val binder = LocalBinder()

    // New variable for tracking the accumulated drift
    private var accumulatedDrift: Float = 0f
    // Threshold for movement (in meters)
    private val movementThreshold: Float = 20f
    // Drift threshold (in meters)
    private val driftThreshold: Float = 5f
    private val accuracyThreshold : Float = 50f

    inner class LocalBinder : Binder() {
        fun getService(): LocationTrackingService = this@LocationTrackingService
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10f, this)
        } catch (e: SecurityException) {
            // Handle exception (permissions not granted)
        }
        startForegroundService()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForegroundService() {
        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("location_service_channel", "Location Service Channel")
        } else {
            ""
        }

        val notification: Notification = Notification.Builder(this, channelId)
            .setContentTitle("Location Service")
            .setContentText("Tracking your location")
            .setSmallIcon(R.drawable.cap)
            .build()

        startForeground(1, notification)
    }

    private fun createNotificationChannel(channelId: String, channelName: String): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            val service = getSystemService(NotificationManager::class.java)
            service.createNotificationChannel(channel)
        }
        return channelId
    }

    override fun onLocationChanged(location: Location) {


        if(location.accuracy > accuracyThreshold){
            return
        }

        lastLocation?.let { lastLoc ->
            val distance = lastLoc.distanceTo(location)
            // Accumulate small movements that might be drift
            if (distance < driftThreshold) {
                accumulatedDrift += distance
                // Check if accumulated drift exceeds the movement threshold
                if (accumulatedDrift >= movementThreshold) {
                    distanceWalked += accumulatedDrift
                    accumulatedDrift = 0f // Reset accumulated drift
                }
            } else {
                // If movement is significant, update distance directly
                distanceWalked += distance
                accumulatedDrift = 0f // Reset accumulated drift
            }
        }
        lastLocation = location
        // You might want to communicate this distance to the UI via a LiveData or similar mechanism
    }

    // Method to reset accumulated drift (can be called if needed)
    fun resetAccumulatedDrift() {
        accumulatedDrift = 0f
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    // Method to get the current distance walked
    fun getDistanceWalked(): Float {
        return distanceWalked
    }

    fun resetDistance() {
        distanceWalked = 0f
    }
}