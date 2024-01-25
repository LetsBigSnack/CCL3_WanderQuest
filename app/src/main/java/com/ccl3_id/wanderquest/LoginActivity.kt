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


class LoginActivity : ComponentActivity() {

    private val permissionHandler = PermissionHandler(this)
    private val db = DatabaseHandler(this)
    private val loginViewModel = LoginViewModel(db)

    private var hasPermissions : Boolean = false

    private val requiredPermissions = arrayOf(
        Manifest.permission.ACTIVITY_RECOGNITION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.FOREGROUND_SERVICE,
        Manifest.permission.POST_NOTIFICATIONS
    )



    private val requiredPermissionsLowerVersion = arrayOf(
        Manifest.permission.ACTIVITY_RECOGNITION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.FOREGROUND_SERVICE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (permissionHandler.hasPermissions(requiredPermissions)) {
            hasPermissions = true
            startLocationTrackingService()
        } else {
            permissionHandler.requestPermissions(requiredPermissions)
        }

        setContent {
            WanderQuestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    loginViewModel.getPlayers();
                    LoginView(loginViewModel,  hasPermissions);
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            startLocationTrackingService()
        } else {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU) {
                if (permissionHandler.hasPermissions(requiredPermissionsLowerVersion)) {
                    hasPermissions = true
                    startLocationTrackingService()
                }else{
                    //TODO Display Explanation why it must be enabled
                }
            }else{
                //TODO Display Explanation why it must be enabled
            }
        }
    }

    private fun startLocationTrackingService() {
        val intent = Intent(this, LocationTrackingService::class.java).apply {
            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        }
        startService(intent)
    }

}