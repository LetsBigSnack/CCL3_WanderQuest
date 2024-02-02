package com.ccl3_id.wanderquest.viewModels

import androidx.lifecycle.ViewModel
import com.ccl3_id.wanderquest.data.DatabaseHandler
import com.ccl3_id.wanderquest.data.models.entities.Player
import com.ccl3_id.wanderquest.viewModels.states.CharacterViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


/**
 * ViewModel for the characters functionality of the application.
 *
 * @author Igor van Duifhuizen / David Kupert
 * @since 01-02-2024
 */
class CharacterViewModel(val db: DatabaseHandler) : ViewModel() {

    // A private MutableStateFlow to hold the state of characters. It's private to protect from external modifications.
    private val _characterViewState = MutableStateFlow(CharacterViewState())

    // A public StateFlow to expose the character state immutably, ensuring it can only be read externally.
    val characterViewState: StateFlow<CharacterViewState> = _characterViewState.asStateFlow()

    /**
     * Fetches characters from the database and updates the character view state.
     */
    fun getCharacters() {
        _characterViewState.update { it.copy(characters = db.getPlayers()) }
    }

    /**
     * Marks a player for deletion and opens the deletion confirmation dialog.
     * @param player The player entity to be marked for deletion.
     */
    fun selectDeleteCharacter(player: Player) {
        _characterViewState.update { it.copy(deletePlayer = player, openPlayerDeleteDialog = true) }
    }

    /**
     * Marks a player for editing and opens the edit dialog.
     * @param player The player entity to be edited.
     */
    fun selectEditCharacter(player: Player) {
        _characterViewState.update { it.copy(editPlayer = player, openPlayerEditDialog = true) }
    }

    /**
     * Dismisses any open dialogs related to player actions.
     */
    fun dismissDialog() {
        _characterViewState.update {
            it.copy(openPlayerEditDialog = false, openPlayerDeleteDialog = false, openPlayerSelectDialog = false)
        }
    }

    /**
     * Deletes the marked player from the database and updates the character view state.
     */
    fun deleteCharacter() {
        characterViewState.value.deletePlayer?.let {
            db.deletePlayer(it)
            _characterViewState.update { currentState ->
                currentState.copy(deletePlayer = null)
            }
            getCharacters()
        }
    }

    /**
     * Updates the marked player's details in the database and refreshes the character view state.
     */
    fun updateCharacter() {
        characterViewState.value.editPlayer?.let {
            db.updatePlayer(it)
            _characterViewState.update { currentState ->
                currentState.copy(editPlayer = null)
            }
            getCharacters()
        }
    }

    /**
     * Marks a player as selected for gameplay and opens the selection confirmation dialog.
     * @param player The player entity to be selected for gameplay.
     */
    fun selectCharacterToPlay(player: Player) {
        _characterViewState.update {
            it.copy(selectedPlayer = player, openPlayerSelectDialog = true)
        }
    }

    /**
     * Confirms the selection of a player for gameplay and updates the character view state.
     */
    fun selectCharacter() {
        characterViewState.value.selectedPlayer?.let {
            db.selectPlayer(it)
            _characterViewState.update { currentState ->
                currentState.copy(selectedPlayer = null)
            }
            getCharacters()
        }
    }
}