package com.ismealdi.visit.view.ui.visit

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.google.maps.android.compose.*
import com.ismealdi.visit.AppActivity
import com.ismealdi.visit.R
import com.ismealdi.visit.data.entity.Store
import com.ismealdi.visit.data.model.LocationData
import com.ismealdi.visit.extension.dateToday
import com.ismealdi.visit.extension.emptyString
import com.ismealdi.visit.navigation.findActivity
import com.ismealdi.visit.view.theme.*
import com.ismealdi.visit.view.ui.components.CustomDialog
import com.ismealdi.visit.viewmodel.VisitViewModel
import org.koin.androidx.compose.koinViewModel

@SuppressLint("MissingPermission")
@Preview(
    name = "Preview",
    device = Devices.PIXEL_2,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    showSystemUi = true
)
@Composable
fun VisitView(
    onLoading: (Boolean) -> Unit = {},
    onDetailClick: (Int) -> Unit = {},
    visitViewModel : VisitViewModel = koinViewModel()
) {

    val loadingEvent by visitViewModel.loadingEvent.observeAsState()

    val locationData by rememberSaveable {
        mutableStateOf(LocationData())
    }

    loadingEvent?.getEventIfNotHandled()?.let { data ->
        onLoading.invoke(data)
    }

    val context = LocalContext.current
    val activity = (context.findActivity() as AppActivity)

    var search by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf( TextFieldValue(emptyString()) )
    }

    activity.locationCallback.let {
        locationData.latitude = activity.locationData.latitude
        locationData.longitude = activity.locationData.longitude
    }

    val stores by visitViewModel.stores.observeAsState()

    var data by remember {
        mutableStateOf(stores?.getEventIfNotHandled())
    }

    stores?.getEventIfNotHandled()?.let { items ->
        data = items
    }

    val lazyListState = rememberLazyListState()

    visitViewModel.fetchingStores()

    val userPosition = LatLng(locationData.latitude ?: 0.0, locationData.longitude ?: 0.0)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userPosition, 14f)
    }

    Column(Modifier.wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 16.dp, end = 16.dp),
            text = search,

            onValueChange = {
                search = it
            },
            label = "Cari Toko/Distributor",
            keyboard = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            leadingIcon = R.drawable.ic_search,
            leadingIconColor = Gray60,
            trailingIcon = {
                if(!search.text.isNullOrEmpty()) {
                    IconButton(onClick = {
                        search = TextFieldValue(emptyString())
                    }) {
                        Icon(
                            Icons.Default.Clear,
                            modifier = Modifier.size(18.dp),
                            contentDescription = stringResource(R.string.clear_text)
                        )
                    }
                }
            }
        )

        Surface(
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentColor = White30,
            shadowElevation = 4.dp
        ) {

            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(zoomControlsEnabled = false, zoomGesturesEnabled = true, compassEnabled = false, mapToolbarEnabled = false, scrollGesturesEnabled = true)
            ) {
                Marker(
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location),
                    state = MarkerState(position = userPosition)
                )

                data?.forEach { store ->
                    val storeLocation = LatLng(store?.latitude?.toDoubleOrNull() ?: 0.0, store?.longitude?.toDoubleOrNull() ?: 0.0)

                    Marker(
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_store_location),
                        state = MarkerState(position = storeLocation)
                    )
                }
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = lazyListState
        ) {
            item {
                Label(
                    "List Kunjungan pada ${dateToday()}",
                    TextAlign.Start, Black60,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    MaterialTheme.typography.titleSmall)
            }

            data?.let { stores ->
                items(stores) { store ->
                    StoreItem(locationData, store, onDetailClick, visitViewModel)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreItem(locationData: LocationData, store: Store,
              onDetailClick: (Int) -> Unit = {}, visitViewModel: VisitViewModel) {

    val yourLocation = LatLng(locationData.latitude ?: 0.0, locationData.longitude ?: 0.0)
    val storeLocation = LatLng(store?.latitude?.toDoubleOrNull() ?: 0.0, store?.longitude?.toDoubleOrNull() ?: 0.0)

    val jarak = SphericalUtil.computeDistanceBetween(yourLocation, storeLocation)
    val distance = String.format("%.1f", jarak / 1000) + " km"

    Surface(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 16.dp, end = 16.dp, bottom = 32.dp),
        contentColor = White30,
        shadowElevation = 4.dp,
        onClick = {
            onDetailClick.invoke(store.storeId)
        }
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(12.dp, 10.dp)) {
            Column(
                Modifier
                    .weight(2f)
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Label(text = store.accountName.toString())
                Label(text = store.regionName.toString(), color = Black60, style = MaterialTheme.typography.bodySmall)
                Label(text = store.areaName.toString(), color = Black60, style = MaterialTheme.typography.bodySmall)
            }

            if(store.visited) {
                Label(modifier = Modifier
                    .weight(0.6f)
                    .wrapContentHeight(), text = stringResource(id = R.string.visited_text), color = Green90, style = MaterialTheme.typography.bodySmall)
            }

            Column(
                Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .padding(end = 12.dp),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_marker),
                    modifier = Modifier
                        .size(24.dp)
                        .padding(bottom = 4.dp),
                    contentDescription = stringResource(R.string.clear_text),
                    tint = Green90
                )
                Label(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .align(CenterHorizontally),
                    text = distance, color = Black60, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
