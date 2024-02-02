package com.ccl3_id.wanderquest.viewModels.states

import com.ccl3_id.wanderquest.data.models.entities.Player

/**
 * Represents the view state for the characters functionality.
 *
 * @author Igor van Duifhuizen / David Kupert
 * @since 01-02-2024
 */
data class CharacterViewState(
    val characters: List<Player> = emptyList(),
    val editPlayer: Player? = null,
    val deletePlayer: Player? = null,
    val selectedPlayer: Player? = null,
    val openPlayerEditDialog: Boolean = false,
    val openPlayerDeleteDialog: Boolean = false,
    val openPlayerSelectDialog: Boolean = false,
)
