package com.ccl3_id.wanderquest.viewModels

import androidx.lifecycle.ViewModel
import com.ccl3_id.wanderquest.data.DatabaseHandler
import com.ccl3_id.wanderquest.data.models.entities.Player
import com.ccl3_id.wanderquest.viewModels.states.LevelUpViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LevelUpViewModel (val db: DatabaseHandler) : ViewModel() {

    private val _levelUpViewState = MutableStateFlow(LevelUpViewState())
    val levelUpViewState: StateFlow<LevelUpViewState> = _levelUpViewState.asStateFlow()

    fun getPlayer() {
        _levelUpViewState.update { it.copy(selectedPlayer = db.getSelectedPlayer()) }
        getPlayerStats();
    }

    fun getPlayerStats(){

        val stats : Map<String, Int> = mutableMapOf(
            Player.STAT_STRENGTH to _levelUpViewState.value.selectedPlayer!!.getStats( Player.STAT_STRENGTH),
            Player.STAT_STAMINA to _levelUpViewState.value.selectedPlayer!!.getStats(Player.STAT_STAMINA),
            Player.STAT_DEXTERITY to _levelUpViewState.value.selectedPlayer!!.getStats( Player.STAT_DEXTERITY),
            Player.STAT_CONSTITUTION to _levelUpViewState.value.selectedPlayer!!.getStats(Player.STAT_CONSTITUTION),
            Player.STAT_MOTIVATION to _levelUpViewState.value.selectedPlayer!!.getStats(Player.STAT_MOTIVATION)
        )

        _levelUpViewState.update {it.copy(stats = stats)}
    }
    fun levelUp() {
        _levelUpViewState.value.selectedPlayer!!.levelUp();
    }

    fun addStat(statName : String){
        _levelUpViewState.value.selectedPlayer!!.addStat(statName);
        getPlayerStats();
    }

    fun subStat(statName: String){
        _levelUpViewState.value.selectedPlayer!!.subStat(statName);
        getPlayerStats();
    }

    fun saveStats(){
        _levelUpViewState.value.selectedPlayer!!.saveStats();
        db.updatePlayer(_levelUpViewState.value.selectedPlayer!!);

    }

}