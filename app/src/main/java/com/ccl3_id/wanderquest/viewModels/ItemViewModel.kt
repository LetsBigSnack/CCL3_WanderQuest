package com.ccl3_id.wanderquest.viewModels

import androidx.lifecycle.ViewModel
import com.ccl3_id.wanderquest.data.DatabaseHandler
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

    fun equipItem(item: Item, playerId: Int){
        val equippedItems = _mainViewState.value.allEquippedItems
        if(equippedItems.find { equippedItem: Item -> equippedItem.type.equals(item.type)} == null){
            db.equipItem(item)
            getEquipItems(playerId)
            getItems(playerId)
        }
        //TODO: Add error msg
    }

    fun unequipItem(equippedItem: Item, playerId: Int){
        db.unequipItem(equippedItem)
        getEquipItems(playerId)
        getItems(playerId)
    }

    fun getEquipItems(playerId: Int){
        val equippedItems = db.getEquipItems(playerId)
        _mainViewState.update { it.copy(allEquippedItems = equippedItems) }
    }

}