package com.ismealdi.visit.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ismealdi.visit.api.network.NetworkResponse
import com.ismealdi.visit.data.model.ErrorData
import com.ismealdi.visit.data.request.LoginRequest
import com.ismealdi.visit.extension.emptyString
import com.ismealdi.visit.helper.Event
import com.ismealdi.visit.helper.postEvent
import com.ismealdi.visit.managers.SessionManager
import com.ismealdi.visit.repository.auth.AuthRepository
import com.ismealdi.visit.repository.store.StoreRepository
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class LoginViewModel(
    private val repository: AuthRepository,
    private val storeRepository: StoreRepository
) : ViewModel(), KoinComponent {

    private val session by inject<SessionManager>()

    private val _errorEvent: MutableLiveData<Event<ErrorData>> = MutableLiveData()
    val errorEvent: LiveData<Event<ErrorData>> = _errorEvent

    private val _loginEvent: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val loginEvent: LiveData<Event<Boolean>> = _loginEvent

    private val _loadingEvent: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val loadingEvent: LiveData<Event<Boolean>> = _loadingEvent

    private val _rememberUsername: MutableLiveData<Event<ErrorData>> = MutableLiveData()
    val rememberUsername: LiveData<Event<ErrorData>> = _rememberUsername

    init {
        if(session.rememberUsername) {
            _rememberUsername.postEvent(ErrorData(session.rememberUsername, session.savedUsername))
        }
    }

    fun performLogin(username: String, password: String) {
        viewModelScope.launch {
            val data = LoginRequest(
                username = username,
                password = password
            )

            if(session.rememberUsername) {
                session.savedUsername = username
            }

            _loadingEvent.postEvent(true)

            repository.auth(data).apply {
                _loadingEvent.postEvent(false)

                when (this) {
                    is NetworkResponse.Success -> {
                        _errorEvent.postEvent(ErrorData(this.response.message.isNotEmpty(), this.response.message))

                        this.response.data?.let { stores ->
                            session.authAccess = UUID.randomUUID().toString()
                            storeRepository.storeInsertAll(stores)
                            _loginEvent.postEvent(true)
                        }
                    }

                    is NetworkResponse.Error -> {
                        Log.d("setalisLog", "Error")
                        _loginEvent.postEvent(false)
                    }
                }
            }
        }
    }

    fun toggleRememberUsername() {
        session.rememberUsername = !session.rememberUsername
        if(!session.rememberUsername) {
            session.savedUsername = emptyString()
        }
        _rememberUsername.postEvent(ErrorData(session.rememberUsername, session.savedUsername))
    }

    fun closeErrorEvent() {
        _errorEvent.postEvent(ErrorData(false))
    }


}