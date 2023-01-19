package com.ismealdi.visit.repository.auth.command

import com.ismealdi.visit.api.dao.StoreDao
import com.ismealdi.visit.api.network.NetworkResponse
import com.ismealdi.visit.api.network.NetworkResponseContract
import com.ismealdi.visit.base.BaseCommand
import com.ismealdi.visit.data.entity.Store
import com.ismealdi.visit.data.request.LoginRequest
import io.ktor.client.call.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.koin.core.component.inject


class LoginCommand : BaseCommand() {

    private val storeDao by inject<StoreDao>()

    suspend fun auth(data: LoginRequest): NetworkResponse<NetworkResponseContract<List<Store>>> {

        return try {
            val response: HttpResponse = network.client().submitForm(
                url = "login/loginTest",
                formParameters = Parameters.build {
                    append("username", data.username)
                    append("password", data.password)
                }
            )

            (NetworkResponse.Success(response.body()))
        } catch (e: Throwable) {
            (NetworkResponse.Error(code.checkError(e)))
        }

    }


}