package com.ccl3_id.wanderquest.viewModels

import androidx.lifecycle.ViewModel
import com.ccl3_id.wanderquest.data.DatabaseHandler
import com.ccl3_id.wanderquest.viewModels.states.LoginViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.flow.update
/**
 * ViewModel for the login functionality of the application.
 *
 *  @author Igor van Duifhuizen / David Kupert
 *  @since 01-02-2024
 *
 * @property db The database handler used for data access.
 */
class LoginViewModel(val db: DatabaseHandler) : ViewModel() {

    // MutableStateFlow to hold the current login view state.
    private val _loginViewState = MutableStateFlow(LoginViewState())

    // Expose the login view state as an immutable StateFlow.
    val loginViewState: StateFlow<LoginViewState> = _loginViewState.asStateFlow()

    /**
     * Fetches the list of players from the database and updates the login view state.
     */
    fun getPlayers() {
        // Update the login view state with the list of players retrieved from the database.
        _loginViewState.update { it.copy(players = db.getPlayers()) }
    }
}