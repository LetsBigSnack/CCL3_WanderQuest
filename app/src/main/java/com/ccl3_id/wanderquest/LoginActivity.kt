package com.ccl3_id.wanderquest

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ccl3_id.wanderquest.data.DatabaseHandler
import com.ccl3_id.wanderquest.handlers.PermissionHandler
import com.ccl3_id.wanderquest.ui.theme.WanderQuestTheme
import com.ccl3_id.wanderquest.ui.views.LoginView
import com.ccl3_id.wanderquest.viewModels.LoginViewModel


class LoginActivity : ComponentActivity() {

    private val permissionHandler = PermissionHandler(this)
    private val db = DatabaseHandler(this)
    private val loginViewModel = LoginViewModel(db)
    private val requiredPermissions = arrayOf(
        Manifest.permission.ACTIVITY_RECOGNITION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.FOREGROUND_SERVICE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (permissionHandler.hasPermissions(requiredPermissions)) {
            println("Permission Granted")
        } else {
            println("Ask Permission")
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
            println("Permission Granted")
        } else {
            println("Denied")
        }
    }
}