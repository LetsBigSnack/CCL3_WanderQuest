package com.ccl3_id.wanderquest

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
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
import com.ccl3_id.wanderquest.services.LocationTrackingService
import com.ccl3_id.wanderquest.ui.theme.WanderQuestTheme
import com.ccl3_id.wanderquest.ui.views.LevelUpView
import com.ccl3_id.wanderquest.viewModels.LevelUpViewModel

class LevelUpActivity : ComponentActivity() {
    private val db = DatabaseHandler(this)
    private val levelUpViewModel = LevelUpViewModel(db)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WanderQuestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    levelUpViewModel.getPlayer();
                    levelUpViewModel.levelUp();
                    LevelUpView(levelUpViewModel, this)
                }
            }
        }
    }
}
