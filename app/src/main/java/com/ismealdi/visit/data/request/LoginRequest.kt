package com.ismealdi.visit.data.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginRequest(
    val username: String,
    val password: String
) : Parcelable

