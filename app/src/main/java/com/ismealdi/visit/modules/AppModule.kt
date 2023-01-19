package com.ismealdi.visit.modules

import android.app.Application
import androidx.room.Room
import com.ismealdi.visit.BuildConfig
import com.ismealdi.visit.api.dao.StoreDao
import com.ismealdi.visit.api.network.Code
import com.ismealdi.visit.api.network.Network
import com.ismealdi.visit.managers.DatabaseManager
import com.ismealdi.visit.managers.SessionManager
import com.ismealdi.visit.repository.auth.AuthRepository
import com.ismealdi.visit.repository.store.StoreRepository
import com.ismealdi.visit.viewmodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

object AppModule : BaseModule {

    override var loadNeeded: Boolean = true

    override fun module() = module {

        single { provideDataBase(androidApplication()) }
        single { storeDao(get()) }

        single(named(KoinModule.Server)) { BuildConfig.server }

        single(named(KoinModule.DispatcherIO)) { Dispatchers.IO }
        single(named(KoinModule.DispatcherMain)) { Dispatchers.Main }

        single(named(KoinModule.CoroutineIo)) { CoroutineScope(Dispatchers.IO + SupervisorJob()) }
        single(named(KoinModule.CoroutineMain)) { CoroutineScope(Dispatchers.Main + SupervisorJob()) }

        // Shared Preference
        single { SessionManager(get()) }

        // Network
        single { Network() }
        single { Code() }

        // Repository
        single { AuthRepository() }
        single { StoreRepository() }

        // ViewModel
        viewModel { SessionViewModel() }
        viewModel { LoginViewModel(get(), get()) }
        viewModel { HomeViewModel(get()) }
        viewModel { VisitViewModel(get()) }
        viewModel { StoreViewModel(get()) }

    }.also {
        this.loadNeeded = false
    }


    private fun provideDataBase(application: Application): DatabaseManager {
        return Room.databaseBuilder(application.applicationContext, DatabaseManager::class.java, BuildConfig.localDatabase)
            .fallbackToDestructiveMigration()
            .build()
    }

    private fun storeDao(dataBase: DatabaseManager): StoreDao {
        return dataBase.storeDao
    }

}
