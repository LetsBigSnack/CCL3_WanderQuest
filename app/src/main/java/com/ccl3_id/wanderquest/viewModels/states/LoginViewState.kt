package com.ccl3_id.wanderquest.viewModels.states

import com.ccl3_id.wanderquest.data.models.entities.Player

/**
 * Represents the view state for the login functionality.
 *
 * @author Igor van Duifhuizen / David Kupert
 * @since 01-02-2024
 */
data class LoginViewState(
    val players: List<Player> = emptyList(),
    val selectedPlayer: Player? = null
)