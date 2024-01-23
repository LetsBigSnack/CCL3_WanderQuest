package com.ccl3_id.wanderquest.viewModels

import androidx.lifecycle.ViewModel
import com.ccl3_id.wanderquest.data.DatabaseHandler
import com.ccl3_id.wanderquest.data.models.entities.Player
import com.ccl3_id.wanderquest.data.models.items.Item
import com.ccl3_id.wanderquest.viewModels.states.MainViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ItemViewModel(private val db: DatabaseHandler) : ViewModel()  {
    private val _mainViewState = MutableStateFlow(MainViewState())
    val mainViewState: StateFlow<MainViewState> = _mainViewState.asStateFlow()

    // Function to fetch items from the database
    fun getItems(playerId: Int) {
        val items = db.getAllItems(playerId)
        _mainViewState.update { it.copy(allItems = items) }
    }

    fun selcetItem(item: Item){
        _mainViewState.update { it.copy(itemClicked = true, clickedItem = item) }
    }

    fun selcetEquippedItem(equippedItem: Item){
        _mainViewState.update { it.copy(equippedItemClicked = true, clickedEquippedItem = equippedItem) }
    }

    fun deselectItem(){
        _mainViewState.update { it.copy(itemClicked = false) }
        _mainViewState.update { it.copy(equippedItemClicked = false) }
    }

    fun equipItem(item: Item, player: Player){
        val tempEquippedItemSlots = _mainViewState.value.equippedItemSlots

        if (tempEquippedItemSlots[item.type] == null){
            tempEquippedItemSlots[item.type] = item
            _mainViewState.update { it.copy(equippedItemSlots = tempEquippedItemSlots) }
            db.equipItem(item)
            item.updateItemStatsFromJSON()
            getEquipItems(player.id)
            getItems(player.id)
        }
        //TODO: Add error msg
    }

    fun replaceEquippedItem(newItem: Item, player: Player){
        val tempEquippedItemSlots = _mainViewState.value.equippedItemSlots.toMutableMap()
        val itemType = newItem.type

        // Unequip the current item in the slot, if present
        tempEquippedItemSlots[itemType]?.let { currentItem ->
            db.unequipItem(currentItem)
        }

        // Equip the new item
        tempEquippedItemSlots[itemType] = newItem
        newItem.updateItemStatsFromJSON()
        db.equipItem(newItem)

        // Update the state
        _mainViewState.update { it.copy(equippedItemSlots = tempEquippedItemSlots) }

        // Refresh the lists
        getEquipItems(player.id)
        getItems(player.id)
    }

    fun unequipItem(equippedItem: Item, player: Player){
        db.unequipItem(equippedItem)
        equippedItem.updateItemStatsFromJSON()
        getEquipItems(player.id)
        getItems(player.id)
    }

    fun getEquipItems(playerId: Int){
        val equippedItemsDb = db.getEquipItems(playerId)
        val equippedItems = mutableMapOf<String, Item?>()

        equippedItems.put("head", equippedItemsDb.find { item: Item -> item.type == "head" })
        equippedItems.put("hand", equippedItemsDb.find { item: Item -> item.type == "hand" })
        equippedItems.put("chest", equippedItemsDb.find { item: Item -> item.type == "chest" })
        equippedItems.put("legs", equippedItemsDb.find { item: Item -> item.type == "legs" })

        _mainViewState.update { it.copy(equippedItemSlots = equippedItems) }
    }

}