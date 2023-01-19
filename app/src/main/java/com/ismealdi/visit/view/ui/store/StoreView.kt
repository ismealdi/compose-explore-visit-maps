package com.ismealdi.visit.view.ui.store

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.ismealdi.visit.AppActivity
import com.ismealdi.visit.BuildConfig
import com.ismealdi.visit.PopUpInfo
import com.ismealdi.visit.R
import com.ismealdi.visit.data.entity.Store
import com.ismealdi.visit.extension.createImageFile
import com.ismealdi.visit.extension.dateToday
import com.ismealdi.visit.extension.emptyString
import com.ismealdi.visit.navigation.findActivity
import com.ismealdi.visit.view.theme.*
import com.ismealdi.visit.view.ui.components.CustomDialog
import com.ismealdi.visit.viewmodel.StoreViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.*
import kotlin.math.roundToInt


@Preview(
    name = "Preview",
    device = Devices.PIXEL_2,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    showSystemUi = true
)
@Composable
fun StoreView(
    storeId: Int? = null,
    onLoading: (Boolean) -> Unit = {},
    onDetailClick: () -> Unit = {},
    storeViewModel : StoreViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val activity = (context.findActivity() as AppActivity)

    var lokasi by rememberSaveable {
        mutableStateOf(context.getString(R.string.lokasi_kosong_text))
    }

    val file = context.createImageFile()

    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        BuildConfig.APPLICATION_ID + ".provider", file
    )

    var currentPhoto by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            currentPhoto = uri
        }

    var currentDistance by rememberSaveable {
        mutableStateOf(0)
    }

    var messageError by rememberSaveable {
        mutableStateOf(emptyString())
    }

    val loadingEvent by storeViewModel.loadingEvent.observeAsState()
    val store by storeViewModel.store.observeAsState()

    var data by remember {
        mutableStateOf(Store())
    }

    val (dialogInfoState, setDialogState) = remember { mutableStateOf(false) }

    store?.getEventIfNotHandled()?.let {
        data = it
    }

    loadingEvent?.getEventIfNotHandled()?.let { data ->
        onLoading.invoke(data)
    }

    val lazyListState = rememberLazyListState()

    if (dialogInfoState) {
        CustomDialog(
            description = messageError,
            onConfirm = {
                setDialogState(!dialogInfoState)
            }
        )
    }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize(),
    ) {
        val (topWave, bottomWave, contentBox, labelBox) = createRefs()

        if(data.pictureVisit.isNullOrEmpty()) {
            Image(
                painter =
                if (currentPhoto.path?.isNotEmpty() == true) { rememberImagePainter(currentPhoto) }
                else { painterResource(id = R.drawable.bg_blue) },
                contentDescription = null, // decorative
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(Primary)
                    .constrainAs(topWave) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
            )
        }else{
            val bytes: ByteArray = Base64.decode(data.pictureVisit, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null, // decorative
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(Primary)
                    .constrainAs(topWave) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(White30)
                .padding()
                .padding(16.dp, 8.dp)
                .constrainAs(bottomWave) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.no_visit_text),
                enabled = true,
                color = Red90,
                action = {
                })

            Button(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.visit_text),
                enabled = (!data.visited && lokasi != stringResource(id = R.string.lokasi_kosong_text)),
                action = {
                    if(currentDistance > 300) {
                        messageError = "Jarak ke Store lebih dari 300m"
                        setDialogState(true)
                    }else{
                        if(currentPhoto.path.isNullOrEmpty()) {
                            messageError = "Mohon ambil photo di Store"
                            setDialogState(true)
                        }else{
                            data.visited = true
                            data.latitudeVisit = activity.locationData.latitude.toString()
                            data.longitudeVisit = activity.locationData.latitude.toString()
                            data.lastVisit = dateToday()
                            data.pictureVisit = Base64.encodeToString(file.readBytes(), Base64.DEFAULT)

                            storeViewModel.updateVisit(data)
                            onDetailClick.invoke()
                        }
                    }
                })
        }

        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(contentBox) {
                top.linkTo(parent.top, 200.dp)
                bottom.linkTo(bottomWave.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
            }) {

            Surface(
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp),
                contentColor = White30,
                shadowElevation = 4.dp,
            ) {
                LazyColumn(
                    Modifier.padding(start = 16.dp, end = 16.dp, top = 38.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start,
                    state = lazyListState
                ) {
                    item {
                        LaunchedEffect(Unit) {
                            storeId?.let { storeViewModel.fetchingStore(it) }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_place),
                                modifier = Modifier.size(18.dp),
                                tint = Orange90,
                                contentDescription = emptyString()
                            )

                            Label(
                                lokasi,
                                TextAlign.Start, Orange90,
                                modifier = Modifier
                                    .wrapContentWidth(),
                                MaterialTheme.typography.bodyMedium)

                            if(lokasi != stringResource(id = R.string.lokasi_kosong_text) && !data.visited) {
                                Label(
                                    "Reset",
                                    TextAlign.Start, Primary,
                                    modifier = Modifier
                                        .clickable {
                                            currentDistance = 0
                                            lokasi = context.getString(R.string.lokasi_kosong_text)
                                        }
                                        .wrapContentWidth(),
                                    MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                    item {
                        Row(modifier = Modifier.padding(top= 24.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_shop),
                                modifier = Modifier.size(18.dp),
                                tint = Orange90,
                                contentDescription = emptyString()
                            )

                            Label(
                                data.accountName.toString(),
                                TextAlign.Start, Black60,
                                modifier = Modifier.width(120.dp),
                                MaterialTheme.typography.bodyLarge)
                        }
                    }


                    item {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Label(
                                "Jalan Pal Merah No.25 Jakarta",
                                TextAlign.Start, Black30,
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .padding(start = 26.dp),
                                MaterialTheme.typography.labelLarge
                            )
                        }

                    }

                    item {
                    LabelItem("Tipe Outlet", data.areaName.toString())
                    }

                    item {
                    LabelItem("Tipe Display", data.regionName.toString())
                    }

                    item {
                    LabelItem("Sub Tipe Display", "Data")
                    LabelItem("ERTM", "Ya")
                    LabelItem("Pareto", "Ya")
                    LabelItem("E-merchandising", "Ya")
                    }

                    item {

                    Row(modifier = Modifier.padding(top= 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_data),
                            modifier = Modifier.size(18.dp),
                            tint = Orange90,
                            contentDescription = emptyString()
                        )

                        Label(
                            "Last Visit",
                            TextAlign.Start, Black60,
                            modifier = Modifier.width(120.dp),
                            MaterialTheme.typography.bodyLarge)
                    }

                    }

                    item {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Label(
                                if (data.visited) data.lastVisit.toString() else "-",
                                TextAlign.Start, Black30,
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .padding(start = 26.dp),
                                MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .wrapContentWidth()
                .height(54.dp)
                .constrainAs(labelBox) {
                    bottom.linkTo(topWave.bottom)
                    end.linkTo(contentBox.end, 34.dp)
                },
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            CircleButton(action = {
                val gmmIntentUri = Uri.parse("google.navigation:q=${data.latitude},${data.longitude}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                context.startActivity(mapIntent)
            }, icon = R.drawable.ic_cursor)

            if(!data.visited) {
                CameraPermission {
                    CircleButton(action = {
                        cameraLauncher.launch(uri)
                    }, icon = R.drawable.ic_camera)
                }

                CircleButton(action = {
                    val yourLocation = LatLng(activity.locationData.latitude ?: 0.0, activity.locationData.longitude ?: 0.0)
                    val storeLocation = LatLng(data?.latitude?.toDoubleOrNull() ?: 0.0, data?.longitude?.toDoubleOrNull() ?: 0.0)

                    val jarak = SphericalUtil.computeDistanceBetween(yourLocation, storeLocation)
                    val distance = String.format("%.1f", jarak / 1000) + " km"

                    currentDistance = jarak.roundToInt()
                    lokasi = activity.locationData.latitude.toString() + "," + activity.locationData.longitude.toString() + " ($distance)"
                }, icon = R.drawable.ic_find_location)
            }else{
                lokasi = data.latitude.toString() + "," + data.longitude.toString()
            }
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermission(content: @Composable () -> Unit) {
    val permissionState = rememberPermissionState(Manifest.permission.CAMERA)

    PermissionRequired(
        permissionState = permissionState,
        permissionNotGrantedContent = {
            PopUpInfo(message = stringResource(id = R.string.this_feature_need_camera)) {
                permissionState.launchPermissionRequest()
            }
        },
        permissionNotAvailableContent = {
            PopUpInfo(message = stringResource(id = R.string.this_feature_need_camera)) {
                permissionState.launchPermissionRequest()
            }
        }) {
        content()
    }
}
