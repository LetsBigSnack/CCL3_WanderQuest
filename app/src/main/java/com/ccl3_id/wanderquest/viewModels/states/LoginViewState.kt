package com.ccl3_id.wanderquest.viewModels.states

import com.ccl3_id.wanderquest.data.models.entities.Player

/**
 * Represents the view state for the login functionality.
 *
 * @author Igor van Duifhuizen / David Kupert
 * @since 01-02-2024
 *
 * @property players The list of players available for selection.
 * @property selectedPlayer The currently selected player (nullable).
 */
data class LoginViewState(
    val players: List<Player> = emptyList(),
    val selectedPlayer: Player? = null
)