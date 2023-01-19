package com.ismealdi.visit.api.dao

import androidx.room.*
import com.ismealdi.visit.data.entity.Store

@Dao
interface StoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stores: List<Store>)

    @Query("DELETE FROM Store")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM Store WHERE visited = 1")
    suspend fun visitedCount() : Int

    @Query("SELECT COUNT(*) FROM Store WHERE visited = 0")
    suspend fun unvisitedCount() : Int

    @Query("SELECT COUNT(*) FROM Store")
    suspend fun storeCount() : Int

    @Query("SELECT * FROM Store")
    suspend fun stores(): List<Store>

    @Query("SELECT * FROM Store WHERE storeId = :storeId LIMIT 1")
    suspend fun store(storeId: Int): Store

    @Update
    suspend fun update(store: Store)

}