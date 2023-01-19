package com.ismealdi.visit.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.*
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
@Entity(tableName = "Store")
data class Store(
    @PrimaryKey @SerializedName("store_id") var storeId: Int = 0,
    @SerializedName("account_id") var accountId: String? = null,
    @SerializedName("account_name") var accountName: String? = null,
    @SerializedName("area_name") var areaName: String? = null,
    @SerializedName("region_name") var regionName: String? = null,

    var latitude: String? = null,
    var longitude: String? = null,

    var visited: Boolean = false,
    var latitudeVisit: String? = null,
    var longitudeVisit: String? = null,
    var lastVisit: String? = null,
    var pictureVisit: String? = null,
) : Parcelable