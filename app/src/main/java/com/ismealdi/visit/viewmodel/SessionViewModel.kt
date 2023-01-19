package com.ismealdi.visit.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ismealdi.visit.extension.immutable
import com.ismealdi.visit.managers.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SessionViewModel : ViewModel(), KoinComponent {

    private val session by inject<SessionManager>()

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin = this._isLogin.immutable()

    init {
        checkLogin()
    }

    private fun checkLogin() {
        this.viewModelScope.launch(Dispatchers.IO) {
            val  authToken = session.authAccess
            _isLogin.postValue((authToken.isNotBlank()) )
        }
    }

    fun loggedIn() = session.authAccess.isNotBlank()

    fun username() = session.savedUsername

}