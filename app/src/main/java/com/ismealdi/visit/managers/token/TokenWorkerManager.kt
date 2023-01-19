package com.ismealdi.visit.managers.token

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.ismealdi.visit.managers.SessionManager
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TokenWorkerManager(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters), KoinComponent {

    private val session by inject<SessionManager>()
    //private val repository by inject<OAuthRepository>()

    override suspend fun doWork(): Result {
        return coroutineScope {
            return@coroutineScope this@TokenWorkerManager.retrieve()
        }
    }

    private suspend fun retrieve(): Result {
        /*repository.auth("-").apply {
            return when (this) {
                is NetworkResponse.Success -> {
                    this.response.data?.accessToken?.let { token ->
                        this@TokenWorkerManager.session.tokenAccess = token
                    }

                    success()
                }

                is NetworkResponse.Error -> {
                    error()
                }
            }
        }*/

        return success()
    }

    private fun success() : Result {
        val data = this.buildData("Token retrieved")
        return Result.success(data)
    }

    private fun error() : Result {
        val data = this.buildData("Token not retrieved")
        return Result.success(data)
    }

    private fun buildData(message: String) =
        Data.Builder()
            .putString("TokenWorkerManager Output: ", message)
            .build()

}