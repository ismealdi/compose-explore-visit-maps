@file:OptIn(ExperimentalMaterial3Api::class)

package com.ismealdi.visit.view.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ismealdi.visit.R
import com.ismealdi.visit.view.theme.Gray30

@Composable
fun CustomDialog(
     onDismissRequest: (() -> Unit)? = null,
     onConfirm: (() -> Unit)? = null,
     onDismiss: (() -> Unit)? = null,
     description: String) {
    Dialog(onDismissRequest = {
        onDismissRequest?.invoke()
    }) {
        CustomDialogUI(onConfirm = onConfirm, onDismiss=onDismiss, description = description)
    }
}

//Layout
@Composable
fun CustomDialogUI(modifier: Modifier = Modifier, onConfirm: (() -> Unit)? = null, onDismiss: (() -> Unit)? = null, description: String){
    Card(
        //shape = MaterialTheme.shapes.medium,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(10.dp,5.dp,10.dp,10.dp)
    ) {
        Column(
            modifier
                .background(Color.White)) {

            onDismiss?.let {
                IconButton(
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.End),
                    onClick = it
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_close),
                        modifier = Modifier.size(28.dp).padding(top=8.dp, bottom = 4.dp, end = 12.dp),
                        contentDescription = stringResource(R.string.clear_text),
                        tint = Gray30
                    )
                }
            }

            //.......................................................................
            Image(
                painter = painterResource(id = R.drawable.ic_exclamation),
                contentDescription = null, // decorative
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(54.dp)
                    .padding(top = 12.dp)
                    .fillMaxWidth(),

                )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.oops_text),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = description,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.labelMedium
                )
            }
            //.......................................................................

            onConfirm?.let {
                com.ismealdi.visit.view.theme.Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
                    text = stringResource(id = R.string.ok_text),
                    action = it
                )
            }
        }
    }
}


@SuppressLint("UnrememberedMutableState")
@Preview (name="Custom Dialog")
@Composable
fun Preview(){
    CustomDialog(
        description = "Text",
        onConfirm = {},
        onDismissRequest = {},
    )
}

