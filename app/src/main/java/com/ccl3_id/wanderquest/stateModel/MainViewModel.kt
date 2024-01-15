package com.ccl3_id.wanderquest.stateModel

import com.ccl3_id.wanderquest.Screen
import com.ccl3_id.wanderquest.data.Item

data class MainViewState (
    val allItems: List<Item> = emptyList(),  // List of Items in the view.
    val selectedScreen: Screen = Screen.First,  // The selected screen/tab in the UI.
)