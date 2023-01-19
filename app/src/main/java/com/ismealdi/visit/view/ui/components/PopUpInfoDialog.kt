package com.ismealdi.visit.view.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.ismealdi.visit.R
import com.ismealdi.visit.view.theme.Black60
import com.ismealdi.visit.view.theme.Button


@Composable
fun PopupInfo(
    @StringRes titleID: Int = R.string.info,
    @StringRes textID: Int,
    @StringRes buttonID: Int = R.string.tutup,
    onDismiss: () -> Unit
) {
    PopupInfo(
        title = stringResource(id = titleID),
        text = stringResource(id = textID),
        button = stringResource(id = buttonID),
        onDismiss = onDismiss
    )
}


@Composable
fun PopupInfo(
    title: String,
    text: String,
    button: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Black60,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = Black60,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },

        dismissButton = {
            Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)) {
                Button(
                    text = button,
                    enabled = true,
                    modifier = Modifier.fillMaxWidth(),
                    action = {
                        onDismiss()
                    }
                )
            }
        },

        confirmButton = {
            Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)) {
                Button(
                    text = stringResource(id = R.string.positive_action_label),
                    enabled = true,
                    modifier = Modifier.fillMaxWidth(),
                    action = {
                        onDismiss()
                    }
                )
            }
        }
    )
}