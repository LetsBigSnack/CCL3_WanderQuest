package com.ccl3_id.wanderquest.viewModels.states

import com.ccl3_id.wanderquest.data.models.entities.Player

data class LoginViewState(
    val players: List<Player> = emptyList(),
    val selectedPlayer: Player? = null,
    )