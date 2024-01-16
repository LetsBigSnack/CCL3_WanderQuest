package com.ccl3_id.wanderquest.viewModels.states

import com.ccl3_id.wanderquest.data.models.entities.Player

data class LevelUpViewState(
    val selectedPlayer: Player? = null,
    val stats : Map<String, Int> = mutableMapOf(
        Player.STAT_STRENGTH to 1,
        Player.STAT_STAMINA to 1,
        Player.STAT_DEXTERITY to 1,
        Player.STAT_CONSTITUTION to 1,
        Player.STAT_MOTIVATION to 1
    )
)