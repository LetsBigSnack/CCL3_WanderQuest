package com.ccl3_id.wanderquest.viewModels.states

import com.ccl3_id.wanderquest.data.models.dungeons.Dungeon
import com.ccl3_id.wanderquest.data.models.entities.Enemy
import com.ccl3_id.wanderquest.data.models.entities.Player
import com.ccl3_id.wanderquest.data.models.items.EquipedItem
import com.ccl3_id.wanderquest.ui.views.Screen
import com.ccl3_id.wanderquest.data.models.items.Item
import com.ccl3_id.wanderquest.data.models.rooms.Room

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
    val allItems: List<Item> = emptyList(),
    val itemClicked: Boolean = false,
    val clickedItem: Item? = null,
    val allEquipedItem: List<EquipedItem> = emptyList(),
    val equipedItemClicked: Boolean = false,
    val clickedEquipedItem: EquipedItem? = null,
    val allOpenDungeons: List<Dungeon> = emptyList(),
    val allActiveDungeon: List<Dungeon> = emptyList(),
    val selectedDungeon: Dungeon? = null,
    var dungeonRooms : Array<Array<Room?>>? = null
)