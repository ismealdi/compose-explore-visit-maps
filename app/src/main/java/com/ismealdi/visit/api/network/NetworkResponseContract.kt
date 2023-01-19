package com.ismealdi.visit.api.network

import com.google.gson.annotations.SerializedName
import com.ismealdi.visit.extension.emptyString

/**
 * Common class used by API response.
 * @param <T> the type of the response object
 */

@kotlinx.serialization.Serializable
data class NetworkResponseContract<T>(

    @SerializedName("stores")
    val data: T?,

    @SerializedName("status")
    val status: String = emptyString(),

    @SerializedName("message")
    val message: String = emptyString()

)