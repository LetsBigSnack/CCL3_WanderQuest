package com.ccl3_id.wanderquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ccl3_id.wanderquest.data.DatabaseHandler
import com.ccl3_id.wanderquest.ui.theme.WanderQuestTheme
import com.ccl3_id.wanderquest.ui.views.CharacterView
import com.ccl3_id.wanderquest.viewModels.CharacterViewModel

/**
 * The Characters-Activity for WanderQuest.
 *
 * @author Igor van Duifhuizen / David Kupert
 * @since 01-02-2024
 */
class CharactersActivity : ComponentActivity() {

    private val db = DatabaseHandler(this)
    private val characterViewModel = CharacterViewModel(db)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WanderQuestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    characterViewModel.getCharacters()
                    CharacterView(characterViewModel)
                }
            }
        }
    }
}