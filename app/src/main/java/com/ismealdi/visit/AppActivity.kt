package com.ismealdi.visit


import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.*
import com.ismealdi.visit.data.model.LocationData
import com.ismealdi.visit.navigation.*
import com.ismealdi.visit.view.theme.AppTheme
import com.ismealdi.visit.view.theme.TopAppBar
import com.ismealdi.visit.view.ui.components.CustomDialog

class AppActivity : ComponentActivity() {

    var locationData: LocationData = LocationData()

    private var fusedLocationClient: FusedLocationProviderClient? = null

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            for (lo in p0.locations) {
                locationData = LocationData(lo.latitude, lo.longitude)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            App() {
                locationUpdate()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun locationUpdate() {
        locationCallback.let {
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                .apply {
                    setWaitForAccurateLocation(false)
                    setMinUpdateIntervalMillis(LocationRequest.Builder.IMPLICIT_MIN_UPDATE_INTERVAL)
                    setMaxUpdateDelayMillis(1000)
                }.build()

            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                it,
                Looper.getMainLooper()
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun App(locationUpdate: () -> Unit) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreenTitle = getTitleByRoute(backStackEntry?.destination?.route)
    val isNoBar = isNoBar(backStackEntry?.destination?.route)
    val isWhiteBar = isWhiteBar(backStackEntry?.destination?.route)
    val subtitle = getSubTitleByRoute(backStackEntry?.destination?.route)
    val icon = icon(backStackEntry?.destination?.route)
    val noBack = noBack(backStackEntry?.destination?.route)

    var loadingState by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    CheckPermission {
        AppTheme {
            Scaffold (
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    if(!isNoBar) {
                        TopAppBar(
                            title = currentScreenTitle,
                            back = navController.previousBackStackEntry != null && !noBack,
                            navigateUp = { navController.navigateUp() },
                            scrollBehavior = scrollBehavior,
                            isWhiteBar = isWhiteBar,
                            subtitle = subtitle,
                            icon = icon
                        )
                    }

                    if(loadingState) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth()
                                .padding(top = if (!isNoBar) 60.dp else 0.dp)
                                .zIndex(10f)
                        )
                    }
                },
            ) {
                AppNavHost(navController, Modifier.padding(it)) {
                    loadingState = it
                }
            }
        }
    }

    locationUpdate.invoke()

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun CheckPermission(content: @Composable () -> Unit) {
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    PermissionRequired(
        permissionState = permissionState,
        permissionNotGrantedContent = {
            PopUpInfo(message = stringResource(id = R.string.this_feature_need_location)) {
                permissionState.launchPermissionRequest()
            }
        },
        permissionNotAvailableContent = {
            PopUpInfo(message = stringResource(id = R.string.this_feature_need_location)) {
                permissionState.launchPermissionRequest()
            }
        }) {
        content()
    }
}

@Composable
fun PopUpInfo(message: String, onClick: () -> Unit) {
    CustomDialog(
        description = message,
        onConfirm = onClick
    )
}