package com.ismealdi.visit.managers

import android.content.Context
import androidx.work.*
import com.ismealdi.visit.managers.token.TokenWorkerManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

class WorkerManager : KoinComponent {

    private val context by inject<Context>()
    private val workerManager = WorkManager.getInstance(this.context)

    fun setupWorkerManager() {
        this.setTokenWorker()
    }

    private fun setTokenWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val tokenWorkName = TokenWorkerManager::class.java.simpleName
        val tokenWorkPolicy = ExistingPeriodicWorkPolicy.REPLACE
        val tokenWorkRequest = PeriodicWorkRequestBuilder<TokenWorkerManager>(5, TimeUnit.DAYS)
            .setConstraints(constraints)
            .addTag(tokenWorkName)
            .build()

        this.workerManager.enqueueUniquePeriodicWork(
            tokenWorkName,
            tokenWorkPolicy,
            tokenWorkRequest
        )
    }

}