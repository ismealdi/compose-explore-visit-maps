package com.ismealdi.visit.managers

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ismealdi.visit.api.dao.StoreDao
import com.ismealdi.visit.data.entity.Store

@Database(entities = [Store::class], version = 1, exportSchema = false)

abstract class DatabaseManager : RoomDatabase() {
    abstract val storeDao: StoreDao
}