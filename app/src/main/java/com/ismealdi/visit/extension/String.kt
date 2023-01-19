package com.ismealdi.visit.extension

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*


fun String?.getValueOrEmpty(): String {
    return when (this) {
        null -> emptyString()
        else -> this
    }
}

fun String?.firstPath(): String? {
    return if(this?.contains("/") == true) {
        this.split("/").first()
    }else{
        this
    }
}

fun emptyString() = ""

fun monthYear(): String? {
    val d = Date(System.currentTimeMillis())
    val cal: Calendar = Calendar.getInstance()
    cal.time = d

    return SimpleDateFormat("MMM yyyy").format(cal.time)
}

fun dateToday(): String? {
    val d = Date(System.currentTimeMillis())
    val cal: Calendar = Calendar.getInstance()
    cal.time = d

    return SimpleDateFormat("dd-MM-yyyy").format(cal.time)
}