package com.ismealdi.visit.repository.store

import com.ismealdi.visit.data.entity.Store
import com.ismealdi.visit.repository.store.command.StoreCommand

class StoreRepository {

    suspend fun update(store: Store) = StoreCommand().update(store)

    suspend fun store(storeId: Int) = StoreCommand().store(storeId)

    suspend fun stores() = StoreCommand().stores()

    suspend fun storeInsertAll(stores: List<Store>) = StoreCommand().storeInsertAll(stores)

    suspend fun storeDeleteAll() = StoreCommand().storeDeleteAll()

    suspend fun storeUnvisitedCount() = StoreCommand().storeUnvisitedCount()

    suspend fun storeVisitedCount() = StoreCommand().storeVisitedCount()

    suspend fun storeCount() = StoreCommand().storeCount()

}