package com.ccl3_id.wanderquest

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ccl3_id.wanderquest.data.DatabaseHandler
import com.ccl3_id.wanderquest.handlers.PermissionHandler
import com.ccl3_id.wanderquest.services.LocationTrackingService
import com.ccl3_id.wanderquest.ui.theme.WanderQuestTheme
import com.ccl3_id.wanderquest.ui.views.LoginView
import com.ccl3_id.wanderquest.viewModels.LoginViewModel
import android.provider.Settings
import androidx.compose.runtime.mutableStateOf

/**
 * The Login-Activity for WanderQuest.
 *
 * @author Igor van Duifhuizen / David Kupert
 * @since 01-02-2024
 */
class LoginActivity : ComponentActivity() {
    private val permissionHandler = PermissionHandler(this)
    private val db = DatabaseHandler(this)
    private val loginViewModel = LoginViewModel(db)

    // Mutable state to track permission status.
    var hasPermissions = mutableStateOf(false)

    // Array of required permissions for the application.
    private val requiredPermissions = arrayOf(
        Manifest.permission.ACTIVITY_RECOGNITION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.FOREGROUND_SERVICE,
        Manifest.permission.POST_NOTIFICATIONS
    )

    // Array of required permissions for lower Android versions < 33.
    private val requiredPermissionsLowerVersion = arrayOf(
        Manifest.permission.ACTIVITY_RECOGNITION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.FOREGROUND_SERVICE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if the required permissions are granted.
        if (permissionHandler.hasPermissions(requiredPermissions)) {
            hasPermissions.value = true
            startLocationTrackingService()
        } else {
            // Request the required permissions if not granted.
            permissionHandler.requestPermissions(requiredPermissions)
        }

        // Set up the user interface using Jetpack Compose.
        setContent {
            WanderQuestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Fetch player data and display the login view.
                    loginViewModel.getPlayers()
                    LoginView(loginViewModel, hasPermissions)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Check if permissions were granted.
        if (permissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            hasPermissions.value = true
            startLocationTrackingService()
        } else {
            // If running on lower Android version, check permissions again.
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU) {
                if (permissionHandler.hasPermissions(requiredPermissionsLowerVersion)) {
                    hasPermissions.value = true
                    startLocationTrackingService()
                }
            }
        }
    }

    /**
     * Start the location tracking service and open app notification settings.
     */
    private fun startLocationTrackingService() {
        val intent = Intent(this, LocationTrackingService::class.java).apply {
            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        }
        startService(intent)
    }

}