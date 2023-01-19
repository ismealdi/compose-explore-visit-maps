package com.ismealdi.visit.view.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.ismealdi.visit.R
import com.ismealdi.visit.data.model.SummaryData
import com.ismealdi.visit.extension.emptyString
import com.ismealdi.visit.extension.monthYear
import com.ismealdi.visit.view.theme.*
import com.ismealdi.visit.view.ui.components.CustomDialog
import com.ismealdi.visit.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Preview(
    name = "Preview",
    device = Devices.PIXEL_2,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    showSystemUi = true
)
@Composable
fun HomeView(
    onLoading: (Boolean) -> Unit = {},
    goToVisit: () -> Unit = {},
    onLogout: () -> Unit = {},
    homeViewModel : HomeViewModel = koinViewModel()
) {

    val userSummary by homeViewModel.userSummary.observeAsState()
    val logoutEvent by homeViewModel.logoutEvent.observeAsState()
    val loadingEvent by homeViewModel.loadingEvent.observeAsState()

    var summaryData by rememberSaveable {
        mutableStateOf(SummaryData())
    }

    userSummary?.getEventIfNotHandled()?.let { data ->
        summaryData = data
    }

    logoutEvent?.getEventIfNotHandled()?.let { data ->
        onLogout.invoke()
    }

    loadingEvent?.getEventIfNotHandled()?.let { data ->
        onLoading.invoke(data)
    }

    val textMenuModifier = Modifier.wrapContentHeight().width(80.dp).padding(top = 4.dp)

    val (dialogInfoState, setDialogState) = remember { mutableStateOf(false) }

    if (dialogInfoState) {
        CustomDialog(
            description = "Ada yakin ingin keluar?",
            onConfirm = {
                setDialogState(!dialogInfoState)
                homeViewModel.perfomLogout()
            },
            onDismiss = {
                setDialogState(!dialogInfoState)
            }
        )
    }

    LazyVerticalGrid(
        modifier = Modifier.background(White30),
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalArrangement = Arrangement.Center
    ) {
        item (span = { GridItemSpan(4) }){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(unbounded = true)
            ) {

                ConstraintLayout(
                    modifier = Modifier.background(color = White60)
                        .wrapContentHeight()
                ) {
                    val (container, photo) = createRefs()

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .paint(
                            painterResource(id = R.drawable.bg_blue_top),
                            contentScale = ContentScale.FillWidth,
                            alignment = Alignment.TopStart
                        )
                        .constrainAs(container) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        })

                    Card(
                        modifier = Modifier
                            .size(80.dp)
                            .constrainAs(photo) {
                                top.linkTo(container.bottom, 48.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                        shape = CircleShape,
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(width = 2.dp, White30)
                    ) {
                        Image(
                            painterResource(R.drawable.ic_person_circle),
                            contentDescription = emptyString(),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .background(White60)
                        .wrapContentHeight()
                        .padding(bottom = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Label(
                        "Aldi Maulana",
                        TextAlign.Center, Gray90,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        MaterialTheme.typography.headlineMedium)

                    Label(
                        "Role: Sales",
                        TextAlign.Center, Gray60,
                        modifier = Modifier.fillMaxWidth(),
                        MaterialTheme.typography.labelLarge)

                    Label(
                        "NIK: MD000101",
                        TextAlign.Center, Gray30,
                        modifier = Modifier.fillMaxWidth(),
                        MaterialTheme.typography.labelLarge)

                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(16.dp),
                        contentColor = White30,
                        shadowElevation = 4.dp
                    ) {

                        Column(
                            modifier = Modifier
                                .padding(8.dp, 10.dp)
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Label(
                                "Kunjungan pada bulan ${monthYear()}",
                                TextAlign.Center, Black60,
                                modifier = Modifier.fillMaxWidth(),
                                MaterialTheme.typography.titleSmall)

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val modifierColumnDashboard = Modifier
                                    .weight(1f)
                                    .wrapContentHeight()

                                Column(modifier = modifierColumnDashboard,
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally) {

                                    TextButton(
                                        modifier = Modifier.wrapContentWidth(),
                                        enabled = false,
                                        onClick = { /* Do something! */ }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_info),
                                            modifier = Modifier
                                                .size(24.dp)
                                                .padding(top = 2.dp),
                                            contentDescription = emptyString(),
                                            tint = Red90
                                        )

                                        Text(text = summaryData.total.toString(),
                                            textAlign = TextAlign.Center,
                                            color = Black60,
                                            modifier = Modifier.padding(start = 8.dp),
                                            style = MaterialTheme.typography.titleLarge)
                                    }

                                    Text(text = "Total Store",
                                        textAlign = TextAlign.Center,
                                        color = Black30,
                                        modifier = Modifier
                                            .height(40.dp),
                                        style = MaterialTheme.typography.bodySmall)

                                }

                                Column(modifier = modifierColumnDashboard,
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally) {

                                    TextButton(
                                        modifier = Modifier.wrapContentWidth(),
                                        enabled = false,
                                        onClick = { /* Do something! */ }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_check),
                                            modifier = Modifier
                                                .size(24.dp)
                                                .padding(top = 2.dp),
                                            contentDescription = emptyString(),
                                            tint = Green90
                                        )

                                        Text(text = summaryData.visited.toString(),
                                            textAlign = TextAlign.Center,
                                            color = Black60,
                                            modifier = Modifier.padding(start = 8.dp),
                                            style = MaterialTheme.typography.titleLarge)
                                    }

                                    Text(text = "Actual Store",
                                        textAlign = TextAlign.Center,
                                        color = Black30,
                                        modifier = Modifier
                                            .height(40.dp),
                                        style = MaterialTheme.typography.bodySmall)
                                }

                                Column(modifier = modifierColumnDashboard,
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally) {

                                    TextButton(
                                        modifier = Modifier.wrapContentWidth(),
                                        enabled = false,
                                        onClick = { /* Do something! */ }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_growth),
                                            modifier = Modifier
                                                .size(24.dp)
                                                .padding(top = 2.dp),
                                            contentDescription = emptyString(),
                                            tint = Yellow90
                                        )

                                        Text(text = "${summaryData.percentage()}%",
                                            textAlign = TextAlign.Center,
                                            color = Black60,
                                            modifier = Modifier.padding(start = 8.dp),
                                            style = MaterialTheme.typography.titleLarge)
                                    }

                                    Text(text = "Percentage",
                                        textAlign = TextAlign.Center,
                                        color = Black30,
                                        modifier = Modifier
                                            .height(40.dp),
                                        style = MaterialTheme.typography.bodySmall)
                                }
                            }

                        }
                    }
                }

                Label(
                    "Menu",
                    TextAlign.Start, Orange90,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 16.dp, bottom = 12.dp),
                    MaterialTheme.typography.titleSmall)

            }
        }


        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.clickable {
                    goToVisit.invoke()
                }.padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    modifier = Modifier
                        .size(44.dp).clip(RoundedCornerShape(50))
                        .background(MenuButtonNormal)
                        .padding(top = 2.dp).padding(8.dp),
                    contentDescription = emptyString(),
                    tint = Green90
                )

                Text(modifier = textMenuModifier, text = "Kunjungan", style =
                MaterialTheme.typography.labelMedium, color = Black60, textAlign = TextAlign.Center)
            }
        }

        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.clickable {  }.padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    modifier = Modifier
                        .size(44.dp).clip(RoundedCornerShape(50))
                        .background(MenuButtonNormal)
                        .padding(top = 2.dp).padding(8.dp),
                    contentDescription = emptyString(),
                    tint = Green90
                )

                Text(modifier = textMenuModifier, text = "Target Install CDFDPC", style =
                MaterialTheme.typography.labelMedium, color = Black60, textAlign = TextAlign.Center)
            }
        }

        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.clickable {  }.padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    modifier = Modifier
                        .size(44.dp).clip(RoundedCornerShape(50))
                        .background(MenuButtonNormal)
                        .padding(top = 2.dp).padding(8.dp),
                    contentDescription = emptyString(),
                    tint = Green90
                )

                Text(modifier = textMenuModifier, text = "Dashboard", style =
                MaterialTheme.typography.labelMedium, color = Black60, textAlign = TextAlign.Center)
            }
        }

        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.clickable {  }.padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    modifier = Modifier
                        .size(44.dp).clip(RoundedCornerShape(50))
                        .background(MenuButtonNormal)
                        .padding(top = 2.dp).padding(8.dp),
                    contentDescription = emptyString(),
                    tint = Green90
                )

                Text(modifier = textMenuModifier, text = "Transaction History", style =
                MaterialTheme.typography.labelMedium, color = Black60, textAlign = TextAlign.Center)
            }
        }

        item(span = { GridItemSpan(4) }) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.clickable {
                    setDialogState(!dialogInfoState)
                }.padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    modifier = Modifier
                        .size(44.dp).clip(RoundedCornerShape(50))
                        .background(MenuButtonNormal)
                        .padding(top = 2.dp).padding(8.dp),
                    contentDescription = emptyString(),
                    tint = Green90
                )

                Text(modifier = textMenuModifier, text = "Logout", style =
                MaterialTheme.typography.labelMedium, color = Black60, textAlign = TextAlign.Center)
            }

            LaunchedEffect(Unit) {
                homeViewModel.fetchingUserSummary()
            }
        }


    }
}