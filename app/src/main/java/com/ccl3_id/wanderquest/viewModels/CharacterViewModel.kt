package com.ccl3_id.wanderquest.viewModels

import androidx.lifecycle.ViewModel
import com.ccl3_id.wanderquest.data.DatabaseHandler
import com.ccl3_id.wanderquest.data.models.entities.Player
import com.ccl3_id.wanderquest.viewModels.states.CharacterViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class CharacterViewModel (val db: DatabaseHandler) : ViewModel() {

    private val _characterViewState = MutableStateFlow(CharacterViewState())
    val characterViewState: StateFlow<CharacterViewState> = _characterViewState.asStateFlow()

    fun getCharacters(){
        _characterViewState.update { it.copy(characters = db.getPlayers()) }
    }

    fun selectDeleteCharacter(player: Player){
        _characterViewState.update { it.copy(deletePlayer = player) }
        _characterViewState.update { it.copy(openPlayerDeleteDialog = true) }
    }

    fun selectEditCharacter(player: Player){
        _characterViewState.update { it.copy(editPlayer = player) }
        _characterViewState.update { it.copy(openPlayerEditDialog = true) }
    }

    fun dismissDialog() {
        _characterViewState.update { it.copy(openPlayerEditDialog = false) }
        _characterViewState.update { it.copy(openPlayerDeleteDialog = false) }
        _characterViewState.update { it.copy(openPlayerSelectDialog = false) }
    }

    fun deleteCharacter(){
        if(characterViewState.value.deletePlayer != null){
            db.deletePlayer(characterViewState.value.deletePlayer!!);
            _characterViewState.update { it.copy(deletePlayer = null) }
            getCharacters();
        }
    }

    fun updateCharacter() {
        if(characterViewState.value.editPlayer != null){

            db.updatePlayer(characterViewState.value.editPlayer!!);
            _characterViewState.update { it.copy(editPlayer = null) }
            getCharacters();
        }
    }

    fun selectCharacterToPlay(player: Player){
        _characterViewState.update { it.copy(selectedPlayer = player) }
        _characterViewState.update { it.copy(openPlayerSelectDialog = true) }
    }

    fun selectCharacter(){

        if(characterViewState.value.selectedPlayer != null){
            db.selectPlayer(characterViewState.value.selectedPlayer!!);
            _characterViewState.update { it.copy(selectedPlayer = null) }
            getCharacters();
        }
    }
}