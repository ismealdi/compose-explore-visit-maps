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

class StoreViewModel(
    private val repository: StoreRepository
) : ViewModel(), KoinComponent {

    private val session by inject<SessionManager>()

    private val _loadingEvent: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val loadingEvent: LiveData<Event<Boolean>> = _loadingEvent

    private val _store: MutableLiveData<Event<Store>> = MutableLiveData()
    val store: LiveData<Event<Store>> = _store

    suspend fun fetchingStore(storeId: Int) {
        viewModelScope.launch {
            _loadingEvent.postEvent(true)
            repository.store(storeId).let { data ->
                _store.postEvent(data)
            }

            _loadingEvent.postEvent(false)
        }
    }

    fun updateVisit(store: Store) {
        viewModelScope.launch() {
            repository.update(store)
        }
    }




}