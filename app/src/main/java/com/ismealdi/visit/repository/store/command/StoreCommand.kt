package com.ismealdi.visit.repository.store.command

import com.ismealdi.visit.api.dao.StoreDao
import com.ismealdi.visit.base.BaseCommand
import com.ismealdi.visit.data.entity.Store
import org.koin.core.component.inject


class StoreCommand : BaseCommand() {

    private val storeDao by inject<StoreDao>()

    suspend fun storeInsertAll(stores: List<Store>) = storeDao.insertAll(stores)
    suspend fun storeDeleteAll() = storeDao.deleteAll()

    suspend fun storeUnvisitedCount() = storeDao.unvisitedCount()
    suspend fun storeVisitedCount() = storeDao.visitedCount()
    suspend fun storeCount() = storeDao.storeCount()

    suspend fun stores() = storeDao.stores()

    suspend fun store(storeId: Int) = storeDao.store(storeId)

    suspend fun update(store: Store) = storeDao.update(store)

}