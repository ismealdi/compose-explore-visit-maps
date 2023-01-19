package com.ismealdi.visit.repository.auth

import com.ismealdi.visit.data.request.LoginRequest
import com.ismealdi.visit.repository.auth.command.LoginCommand

class AuthRepository {

    suspend fun auth(request: LoginRequest) = LoginCommand().auth(request)

}