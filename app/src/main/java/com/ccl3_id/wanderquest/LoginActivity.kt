package com.ccl3_id.wanderquest

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
import com.ccl3_id.wanderquest.ui.theme.WanderQuestTheme
import com.ccl3_id.wanderquest.ui.views.LoginView
import com.ccl3_id.wanderquest.viewModels.LoginViewModel

class LoginActivity : ComponentActivity() {
    private val db = DatabaseHandler(this)
    private val loginViewModel = LoginViewModel(db)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}