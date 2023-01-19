package com.ismealdi.visit.view.ui.summary

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.location.*
import com.google.maps.android.compose.*
import com.ismealdi.visit.view.theme.*

@SuppressLint("MissingPermission")
@Preview(
    name = "Preview",
    device = Devices.PIXEL_2,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    showSystemUi = true
)
@Composable
fun SummaryView(
    onLoading: (Boolean) -> Unit = {},
    onFinishClick: (Int) -> Unit = {}
) {

}
