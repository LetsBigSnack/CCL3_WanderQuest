package com.ccl3_id.wanderquest.viewModels

import androidx.lifecycle.ViewModel
import com.ccl3_id.wanderquest.data.DatabaseHandler
import com.ccl3_id.wanderquest.viewModels.states.LoginViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel (val db: DatabaseHandler) : ViewModel() {

    private val _loginViewState = MutableStateFlow(LoginViewState())
    val loginViewState: StateFlow<LoginViewState> = _loginViewState.asStateFlow()

    fun getPlayers(){
        _loginViewState.update { it.copy(players = db.getPlayers()) }
    }

}