package com.ccl3_id.wanderquest.viewModels

import androidx.lifecycle.ViewModel
import com.ccl3_id.wanderquest.ui.views.Screen
import com.ccl3_id.wanderquest.data.DatabaseHandler
import com.ccl3_id.wanderquest.data.models.items.EquipedItem
import com.ccl3_id.wanderquest.data.models.items.Item
import com.ccl3_id.wanderquest.viewModels.states.MainViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
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

    fun selcetEquipedItem(equipedItem: EquipedItem){
        _mainViewState.update { it.copy(equipedItemClicked = true, clickedEquipedItem = equipedItem) }
    }

    fun deselectItem(){
        _mainViewState.update { it.copy(itemClicked = false) }
        _mainViewState.update { it.copy(equipedItemClicked = false) }
    }

    fun equipItem(item: Item, playerId: Int){
        db.equipItem(item)
        getEquipItems(playerId)
        getItems(playerId)
    }

    fun unequipItem(equippedItem: Item, playerId: Int){
        db.unequipItem(equippedItem)
        getEquipItems(playerId)
        getItems(playerId)
    }

    fun getEquipItems(playerId: Int){
        val equipedItems = db.getEquipItems(playerId)
        _mainViewState.update { it.copy(allEquipedItem = equipedItems) }
    }

}