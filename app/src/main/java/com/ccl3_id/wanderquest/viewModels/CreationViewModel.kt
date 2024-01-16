package com.ccl3_id.wanderquest.viewModels

import androidx.lifecycle.ViewModel
import com.ccl3_id.wanderquest.data.DatabaseHandler
import com.ccl3_id.wanderquest.data.models.entities.Player
import com.ccl3_id.wanderquest.viewModels.states.CreationViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class CreationViewModel (val db: DatabaseHandler) : ViewModel() {

    private val _creationViewState = MutableStateFlow(CreationViewState())
    val creationViewState: StateFlow<CreationViewState> = _creationViewState.asStateFlow()

    fun nextStep(){
        _creationViewState.update { it.copy(stepNumber = creationViewState.value.stepNumber+1) }
        println(creationViewState.value.stepNumber);
    }

    fun previousStep(){
        _creationViewState.update { it.copy(stepNumber = creationViewState.value.stepNumber-1) }
        println(creationViewState.value.stepNumber);
    }

    fun changeCharacterName(name : String){
        _creationViewState.update { it.copy(characterName = name) }
    }
    fun selectClass(selectedClass: String){
        _creationViewState.update { it.copy(characterClass = selectedClass) }
    }

    fun addStat(statName : String){
        if(creationViewState.value.statPoints >= 1){
            _creationViewState.update { currentState ->
                val updatedStats = currentState.stats.toMutableMap().apply {
                    this[statName] = (this[statName] ?: 0) + 1
                }
                currentState.copy(stats = updatedStats)
            }
            spendStatPoints();
        }
    }

    fun subStat(statName : String){
        if(creationViewState.value.stats[statName]!! > 1){
            _creationViewState.update { currentState ->
                val updatedStats = currentState.stats.toMutableMap().apply {
                    this[statName] = (this[statName] ?: 0) - 1
                }
                currentState.copy(stats = updatedStats)
            }
            refundStatPoints()
        }
    }

    fun refundStatPoints(){
        _creationViewState.update { it.copy(statPoints = creationViewState.value.statPoints+1) }
    }

    fun spendStatPoints(){
        _creationViewState.update { it.copy(statPoints = creationViewState.value.statPoints-1) }
    }

    fun createCharacter(){

        val stats : MutableMap<String, Int> = mutableMapOf(
            Player.STAT_STRENGTH to  creationViewState.value.stats[Player.STAT_STRENGTH]!!,
            Player.STAT_STAMINA to  creationViewState.value.stats[Player.STAT_STAMINA]!!,
            Player.STAT_DEXTERITY to creationViewState.value.stats[Player.STAT_DEXTERITY]!!,
            Player.STAT_CONSTITUTION to creationViewState.value.stats[Player.STAT_CONSTITUTION]!!,
            Player.STAT_MOTIVATION to creationViewState.value.stats[Player.STAT_MOTIVATION]!!,
        )

        var createdPlayer : Player = Player(
            creationViewState.value.characterName,
            creationViewState.value.characterClass,
            1,
            stats,
            creationViewState.value.statPoints,
            true
        )

        db.insertPlayer(createdPlayer);
    }
}