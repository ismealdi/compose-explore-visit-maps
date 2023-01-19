package com.ismealdi.visit.managers

import android.content.Context
import androidx.preference.PreferenceManager
import com.ismealdi.visit.extension.emptyString
import com.ismealdi.visit.extension.getValueOrEmpty
import com.ismealdi.visit.managers.constant.ConstantSessions

class SessionManager(
    private val context: Context
) {
    private val preferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this.context)
    }

    var tokenAccess: String
        get() = this.preferences
            .getString(ConstantSessions.TOKEN_KEY, emptyString())
            .getValueOrEmpty()
        set(value) = this.preferences
            .edit()
            .putString(ConstantSessions.TOKEN_KEY, value)
            .apply()

    var authAccess: String
        get() = this.preferences
            .getString(ConstantSessions.AUTH_KEY, emptyString())
            .getValueOrEmpty()
        set(value) = this.preferences
            .edit()
            .putString(ConstantSessions.AUTH_KEY, value)
            .apply()

    var rememberUsername: Boolean
        get() = this.preferences
            .getBoolean(ConstantSessions.REMEMBER_USERNAME_KEY, false)
        set(value) = this.preferences
            .edit()
            .putBoolean(ConstantSessions.REMEMBER_USERNAME_KEY, value)
            .apply()

    var savedUsername: String
        get() = this.preferences
            .getString(ConstantSessions.SAVED_USERNAME_KEY, emptyString()).getValueOrEmpty()
        set(value) = this.preferences
            .edit()
            .putString(ConstantSessions.SAVED_USERNAME_KEY, value)
            .apply()

}