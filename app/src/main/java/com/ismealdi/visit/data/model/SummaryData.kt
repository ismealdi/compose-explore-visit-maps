package com.ismealdi.visit.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.math.round

@kotlinx.serialization.Serializable
@Parcelize
data class SummaryData(
    var visited: Int = 0,
    var unvisited: Int = 0,
    var total: Int = 0
) : Parcelable {
    fun percentage() = if(visited > 0) round(((visited / total ) * 100).toDouble()).toInt() else 0
}