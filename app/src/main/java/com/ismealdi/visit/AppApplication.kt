package com.ismealdi.visit

import android.app.Application
import com.ismealdi.visit.managers.WorkerManager
import com.ismealdi.visit.modules.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Koin
        setKoin()

        // Worker Manager
        this.setWorkerManager()
    }

    private fun setKoin() {
        startKoin {
            this.androidContext(this@AppApplication)
            this.androidLogger(Level.NONE)
            this.androidFileProperties()

            modules(AppModule.module()).also {
                AppModule.loadNeeded = false
            }
        }
    }

    private fun setWorkerManager() {
        WorkerManager().setupWorkerManager()
    }
}