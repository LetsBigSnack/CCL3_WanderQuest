package com.ccl3_id.wanderquest.viewModels.states

import com.ccl3_id.wanderquest.data.models.GameObject
import com.ccl3_id.wanderquest.data.models.dungeons.Dungeon
import com.ccl3_id.wanderquest.data.models.entities.Enemy
import com.ccl3_id.wanderquest.data.models.entities.Player
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
    val allEquippedItems: List<Item> = emptyList(),
    val equippedItemSlots: MutableMap<String, Item?> = mutableMapOf(
        "head" to null,
        "hand" to null,
        "chest" to null,
        "legs" to null
    ),
    val equippedItemClicked: Boolean = false,
    val clickedEquippedItem: Item? = null,
    val allOpenDungeons: List<Dungeon> = emptyList(),
    val allActiveDungeon: List<Dungeon> = emptyList(),
    val selectedDungeon: Dungeon? = null,
    var dungeonRooms : Array<Array<Room?>>? = null,
    var adjacentRooms : List<Room> = emptyList(),
    var currentSelectedRoom : Room? = null,
    var displayDungeonPopUp : Boolean = false,
    var currentRoomContent : GameObject? = null,
    val openPlayerDeleteDialog: Boolean = false,
    val openPlayerEditDialog: Boolean = false,
    val displayCompleteDialog: Boolean = false,
    val dungeonDeleteDialog: Boolean = false,
    val deletedDungeon: Dungeon? = null
) {

}