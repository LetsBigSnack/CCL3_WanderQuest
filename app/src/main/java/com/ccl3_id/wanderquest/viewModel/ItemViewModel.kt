package com.ccl3_id.wanderquest.viewModel

import androidx.lifecycle.ViewModel
import com.ccl3_id.wanderquest.Screen
import com.ccl3_id.wanderquest.data.Item
import com.ccl3_id.wanderquest.data.ItemHandler
import com.ccl3_id.wanderquest.stateModel.MainViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class ItemViewModel(private val db: ItemHandler) : ViewModel()  {
    private val _mainViewState = MutableStateFlow(MainViewState())
    val mainViewState: StateFlow<MainViewState> = _mainViewState.asStateFlow()

    private val _itemsState = MutableStateFlow<List<Item>>(emptyList())
    val getItemsState: StateFlow<List<Item>> = _itemsState.asStateFlow()

    // Function to fetch items from the database
    fun getItems() {
        val items = db.getAllItems()
        _itemsState.value = items
    }

    private val _selectedItems = MutableStateFlow<Set<Item>>(emptySet())
    val selectedItems: StateFlow<Set<Item>> = _selectedItems.asStateFlow()

    // Function to check if an item is selected
    fun isItemSelected(item: Item): Flow<Boolean> {
        return selectedItems.map { selectedItemsSet ->
            selectedItemsSet.contains(item)
        }
    }

    // Function to get the selected items
    fun getSelectedItems(): List<Item> {
        return _selectedItems.value.toList()
    }

    // Function to toggle the selection state of an item
    fun toggleItemSelection(item: Item, isSelected: Boolean) {
        _selectedItems.value = if (isSelected) {
            _selectedItems.value + item
        } else {
            _selectedItems.value - item
        }
    }

    // Select a screen in the UI.
    fun selectScreen(screen: Screen) {
        _mainViewState.update { it.copy(selectedScreen = screen) }
    }

}