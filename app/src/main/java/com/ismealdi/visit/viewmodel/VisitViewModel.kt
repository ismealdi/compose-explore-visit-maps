package com.ismealdi.visit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ismealdi.visit.data.entity.Store
import com.ismealdi.visit.helper.Event
import com.ismealdi.visit.helper.postEvent
import com.ismealdi.visit.managers.SessionManager
import com.ismealdi.visit.repository.store.StoreRepository
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class VisitViewModel(
    private val repository: StoreRepository
) : ViewModel(), KoinComponent {

    private val session by inject<SessionManager>()

    private val _loadingEvent: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val loadingEvent: LiveData<Event<Boolean>> = _loadingEvent

    private val _stores: MutableLiveData<Event<List<Store>>> = MutableLiveData()
    val stores: LiveData<Event<List<Store>>> = _stores

    fun fetchingStores() {
        viewModelScope.launch {
            _loadingEvent.postEvent(true)
            val data = repository.stores()
            _stores.postEvent(data)
            _loadingEvent.postEvent(false)
        }
    }




}