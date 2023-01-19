package com.ismealdi.visit.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@kotlinx.serialization.Serializable
@Parcelize
data class LocationData(
    var latitude: Double? = null,
    var longitude: Double? = null
) : Parcelable