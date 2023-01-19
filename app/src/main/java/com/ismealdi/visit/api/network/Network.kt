package com.ismealdi.visit.api.network

import android.util.Log
import com.ismealdi.visit.managers.SessionManager
import com.ismealdi.visit.modules.KoinModule
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import kotlinx.serialization.serializer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class Network : KoinComponent {

    private val server by inject<String>(named(KoinModule.Server))
    private val session by inject<SessionManager>()

    private val client = HttpClient(Android) {

        engine {
            connectTimeout = 30_000
            socketTimeout = 30_000
        }

        // Response logs
        install(Logging) {
            logger = CustomHttpLogger()
            level = LogLevel.ALL
        }

        // Header
        install(DefaultRequest) {
            /*header("Accept", "application/json")
            header("Content-type", "application/json")

            if(session.tokenAccess.isNotEmpty()) {
                header("Authorization", "Bearer ${session.tokenAccess}")
            }*/

            contentType(ContentType.Application.FormUrlEncoded)

        }

        // Json
        install(ContentNegotiation) {
            gson(ContentType.Any) {
                disableHtmlEscaping()
                serializer<kotlinx.serialization.Serializable>()
            }
        }

        //Print other logs
        install(ResponseObserver) {
            onResponse { response ->
                Log.d("setalisLog", "HTTP status: ${response.status.value}")
            }
        }

        defaultRequest {
            url {
                protocol =URLProtocol.HTTPS
                host = server
            }
        }


    }

    fun client() = client

}

class CustomHttpLogger : Logger {
    override fun log(message: String) {
        Log.d("loggerTag", message)
    }
}