package com.ismealdi.visit.view.ui.summary

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.google.android.gms.location.*
import com.google.maps.android.compose.*
import com.ismealdi.visit.AppActivity
import com.ismealdi.visit.R
import com.ismealdi.visit.data.entity.Store
import com.ismealdi.visit.extension.emptyString
import com.ismealdi.visit.navigation.findActivity
import com.ismealdi.visit.view.theme.*
import com.ismealdi.visit.viewmodel.StoreViewModel
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
fun SummaryView(
    storeId: Int? = null,
    onLoading: (Boolean) -> Unit = {},
    onFinishClick: () -> Unit = {},
    storeViewModel : StoreViewModel = koinViewModel()
) {

    val context = LocalContext.current
    val activity = (context.findActivity() as AppActivity)

    val loadingEvent by storeViewModel.loadingEvent.observeAsState()
    val store by storeViewModel.store.observeAsState()

    var data by remember {
        mutableStateOf(Store())
    }

    store?.getEventIfNotHandled()?.let {
        data = it
    }

    loadingEvent?.getEventIfNotHandled()?.let { data ->
        onLoading.invoke(data)
    }

    val textMenuModifier = Modifier
        .wrapContentHeight()
        .width(80.dp)
        .padding(top = 4.dp)

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {

        val (bottomWave, contentBox) = createRefs()

        LaunchedEffect(Unit) {
            storeId?.let { storeViewModel.fetchingStore(it) }
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
                        .background(White60)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    MarqueeText(
                        modifier = Modifier
                            .background(White30)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        textModifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 8.dp),
                        color = Primary,
                        text = "Terimakasih dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt"
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .background(Primary, RoundedCornerShape(4.dp))
                            .padding(12.dp)
                    ) {
                        if(!data.pictureVisit.isNullOrEmpty()) {
                            val bytes: ByteArray = Base64.decode(data.pictureVisit, Base64.DEFAULT)
                            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = null, // decorative
                                alignment = Alignment.Center,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(70.dp))
                                    .background(Secondary, RoundedCornerShape(70.dp))
                            )
                        }

                        Column(Modifier.fillMaxWidth().padding(start = 12.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {

                            Label(
                                text = data.accountId.toString(),
                                textAlign = TextAlign.Start, color = White60,
                                style = MaterialTheme.typography.bodyLarge)

                            Label(
                                text = data.accountName.toString(),
                                textAlign = TextAlign.Start, color = White30,
                                style = MaterialTheme.typography.bodyMedium)

                            Label(
                                text = data.areaName.toString(),
                                textAlign = TextAlign.Start, color = White30,
                                style = MaterialTheme.typography.bodyMedium)

                            Label(
                                text = data.lastVisit.toString(),
                                textAlign = TextAlign.Start, color = White30,
                                style = MaterialTheme.typography.bodyMedium)

                        }
                    }

                    Label(
                        "Menu",
                        TextAlign.Start, Orange90,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(White30)
                            .padding(start = 16.dp, top = 12.dp, bottom = 12.dp),
                        MaterialTheme.typography.titleSmall
                    )
                }
            }

            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clickable {

                        }
                        .padding(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_info),
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Red30)
                            .padding(top = 2.dp)
                            .padding(8.dp),
                        contentDescription = emptyString(),
                        tint = White30
                    )

                    Text(modifier = textMenuModifier, text = "Information", style =
                    MaterialTheme.typography.labelMedium, color = Black60, textAlign = TextAlign.Center)
                }
            }

            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clickable {

                        }
                        .padding(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_info),
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Green90)
                            .padding(top = 2.dp)
                            .padding(8.dp),
                        contentDescription = emptyString(),
                        tint = White30
                    )

                    Text(modifier = textMenuModifier, text = "Product Check", style =
                    MaterialTheme.typography.labelMedium, color = Black60, textAlign = TextAlign.Center)
                }
            }

            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clickable {

                        }
                        .padding(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_info),
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Danger)
                            .padding(top = 2.dp)
                            .padding(8.dp),
                        contentDescription = emptyString(),
                        tint = White30
                    )

                    Text(modifier = textMenuModifier, text = "SKU Promo", style =
                    MaterialTheme.typography.labelMedium, color = Black60, textAlign = TextAlign.Center)
                }
            }

            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clickable {

                        }
                        .padding(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_info),
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Orange90)
                            .padding(top = 2.dp)
                            .padding(8.dp),
                        contentDescription = emptyString(),
                        tint = White30
                    )

                    Text(modifier = textMenuModifier, text = "Ringkasan POS", style =
                    MaterialTheme.typography.labelMedium, color = Black60, textAlign = TextAlign.Center)
                }
            }

            item(span = { GridItemSpan(4) }) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clickable {

                        }
                        .padding(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_data),
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Primary)
                            .padding(top = 2.dp)
                            .padding(8.dp),
                        contentDescription = emptyString(),
                        tint = White30
                    )

                    Text(modifier = textMenuModifier, text = "Store Investment", style =
                    MaterialTheme.typography.labelMedium, color = Black60, textAlign = TextAlign.Center)
                }
            }

            item(span = { GridItemSpan(4) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(White30),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Label(
                        "Dashboard Store",
                        TextAlign.Start, Orange90,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp, top = 12.dp, bottom = 12.dp),
                        MaterialTheme.typography.titleSmall
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        modifier = Modifier
                            .size(16.dp)
                            .clip(RoundedCornerShape(50)),
                        contentDescription = emptyString(),
                        tint = Red90
                    )

                    Label(
                        "Perfect Store",
                        TextAlign.End, Black90,
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(start = 8.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
                        MaterialTheme.typography.titleSmall
                    )
                }
            }

            val itemModifier = Modifier.width(120.dp).height(200.dp).background(Primary, RoundedCornerShape(4.dp)).padding(12.dp)

            item(span = { GridItemSpan(4) }) {
                LazyHorizontalGrid(
                    modifier = Modifier.height(200.dp).background(
                        White30),
                    rows = GridCells.Adaptive(120.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text("Single item", itemModifier)
                    }
                    item {
                        Text("Single item", itemModifier)
                    }
                    item {
                        Text("Single item", itemModifier)
                    }
                    item {
                        Text("Single item", itemModifier)
                    }
                    item {
                        Text("Single item", itemModifier)
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(White60)
                .padding()
                .padding(16.dp, 8.dp)
                .constrainAs(bottomWave) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Button(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.selesai_text),
                enabled = true,
                action = {
                    onFinishClick.invoke()
                })
        }
    }
}
