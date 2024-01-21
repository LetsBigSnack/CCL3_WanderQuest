package com.ccl3_id.wanderquest

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
    private val requiredPermissions = arrayOf(
        Manifest.permission.ACTIVITY_RECOGNITION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.FOREGROUND_SERVICE,
        Manifest.permission.POST_NOTIFICATIONS
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (permissionHandler.hasPermissions(requiredPermissions)) {
            startLocationTrackingService()
        } else {
            permissionHandler.requestPermissions(requiredPermissions)
        }

        setContent {
            WanderQuestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    loginViewModel.getPlayers();
                    LoginView(loginViewModel, this);
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            startLocationTrackingService()
        } else {
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