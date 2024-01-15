package com.ccl3_id.wanderquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ccl3_id.wanderquest.data.ItemHandler
import com.ccl3_id.wanderquest.ui.theme.WanderQuestTheme
import com.ccl3_id.wanderquest.viewModel.ItemViewModel


// The main activity of the application.
class MainActivity : ComponentActivity() {
    // Database handler for Items.
    private val itemDb = ItemHandler(this)

    // ViewModel for the Items view.
    private val itemViewModel = ItemViewModel(itemDb)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WanderQuestTheme {
                // A surface container using the 'background' color from the theme.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Initialize and fetch Pokemon trainers from the database.
                    itemViewModel.getItems()

                    // Create and display the main view with associated ViewModels.
                    MainView(itemViewModel)
                }
            }
        }
    }
}