package com.ccl3_id.wanderquest.viewModels.states

import com.ccl3_id.wanderquest.data.models.dungeons.Dungeon
import com.ccl3_id.wanderquest.data.models.entities.Enemy
import com.ccl3_id.wanderquest.data.models.entities.Player
import com.ccl3_id.wanderquest.ui.views.Screen
import com.ccl3_id.wanderquest.data.models.items.Item

data class MainViewState (
    val selectedPlayer: Player? = null,
    val selectedScreen: Screen = Screen.Character,
    val battleStarted : Boolean = false,
    val battleComplete: Boolean = false,
    val enemy: Enemy? = null,
    var playerText :String = "",
    val enemyText : String="",
    val battleCompleteText : String = "",
    val currentPlayerHealth : Int = 1,
    val currentEnemyHealth : Int = 1,
    val allItems: List<Item> = emptyList(),  // List of Items in the view.
    val allOpenDungeons: List<Dungeon> = emptyList(),
    val allActiveDungeon: List<Dungeon> = emptyList()
)