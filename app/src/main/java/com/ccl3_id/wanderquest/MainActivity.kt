package com.ccl3_id.wanderquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ccl3_id.wanderquest.data.DatabaseHandler
import com.ccl3_id.wanderquest.data.models.dungeons.Dungeon
import com.ccl3_id.wanderquest.repository.LocationRepository
import com.ccl3_id.wanderquest.ui.theme.WanderQuestTheme
import com.ccl3_id.wanderquest.ui.views.MainView
import com.ccl3_id.wanderquest.viewModels.ItemViewModel
import com.ccl3_id.wanderquest.viewModels.MainViewModel


// The main activity of the application.
class MainActivity : ComponentActivity() {
    // Database handler for Items.
    private val db = DatabaseHandler(this)
    // ViewModel for the Items view.

    private val itemViewModel = ItemViewModel(db)
    private lateinit var mainViewModel : MainViewModel
    private lateinit var locationRepository: LocationRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationRepository = LocationRepository(this)
        mainViewModel = MainViewModel(db, locationRepository)

        setContent {
            WanderQuestTheme {
                // A surface container using the 'background' color from the theme.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Initialize and fetch Pokemon trainers from the database.
                    // Create and display the main view with associated ViewModels.
                    mainViewModel.getPlayer()
                    MainView(mainViewModel, itemViewModel)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationRepository.unbindService(this)
    }
}