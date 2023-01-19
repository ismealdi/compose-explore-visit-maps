package com.ismealdi.visit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ismealdi.visit.data.model.SummaryData
import com.ismealdi.visit.extension.emptyString
import com.ismealdi.visit.helper.Event
import com.ismealdi.visit.helper.postEvent
import com.ismealdi.visit.managers.SessionManager
import com.ismealdi.visit.repository.store.StoreRepository
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeViewModel(
    private val repository: StoreRepository
) : ViewModel(), KoinComponent {

    private val session by inject<SessionManager>()

    private val _loadingEvent: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val loadingEvent: LiveData<Event<Boolean>> = _loadingEvent

    private val _logoutEvent: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val logoutEvent: LiveData<Event<Boolean>> = _logoutEvent

    private val _userSummary: MutableLiveData<Event<SummaryData>> = MutableLiveData()
    val userSummary: LiveData<Event<SummaryData>> = _userSummary

    suspend fun fetchingUserSummary() {
        viewModelScope.launch {
            val summaryData = SummaryData(repository.storeVisitedCount(), repository.storeUnvisitedCount(), repository.storeCount())
            _userSummary.postEvent(summaryData)
        }
    }

    fun perfomLogout() {
        viewModelScope.launch {
            _loadingEvent.postEvent(true)

            session.authAccess = emptyString()
            repository.storeDeleteAll()

            _loadingEvent.postEvent(false)
            _logoutEvent.postEvent(true)
        }
    }




}