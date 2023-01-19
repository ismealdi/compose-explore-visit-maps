@file:OptIn(ExperimentalMaterial3Api::class)

package com.ismealdi.visit.view.theme

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ismealdi.visit.R
import com.ismealdi.visit.extension.emptyString

@Composable
fun TopAppBar(
    title: String,
    back: Boolean? = false,
    navigateUp: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    isWhiteBar: Boolean = false,
    subtitle: String = emptyString(),
    icon: Int? = null
) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = if(isWhiteBar) White30 else Transparent,
            scrolledContainerColor = if(isWhiteBar) White60 else Primary,
            titleContentColor = if(isWhiteBar) Black60 else White30,
            actionIconContentColor = if(isWhiteBar) Black60 else White30
        ),

        scrollBehavior = scrollBehavior,

        modifier = Modifier
                .paint(painterResource(id = R.drawable.bg_top_bar),
                contentScale = ContentScale.FillBounds,
                alignment = Alignment.BottomStart),

        title = {
            Column(modifier = Modifier
                .height(48.dp)
                .padding(top = 16.dp), verticalArrangement = Arrangement.Center) {
                Text(
                    title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = topBarText
                )

                if(subtitle.isNotEmpty()) {
                    Text(
                        subtitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = topBarSubtitleText
                    )
                }
            }
        },
        navigationIcon = {
            if(back == true) {
                IconButton(onClick = navigateUp, modifier = Modifier
                    .height(48.dp)
                    .padding(top = 16.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        modifier = Modifier.size(18.dp),
                        tint = if(isWhiteBar) Black60 else White60,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if(icon != null) {
                IconButton(onClick = {    }, modifier = Modifier
                    .height(48.dp)
                    .padding(top = 16.dp)) {
                    Icon(
                        painter = painterResource(id = icon),
                        modifier = Modifier.size(20.dp),
                        contentDescription = "Menu"
                    )
                }
            }

        }
    )
}

@Composable
fun LabelTitle(
    text: String
) {
    Text(text, color = Black60,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        style = MaterialTheme.typography.headlineMedium)
}

@Composable
fun LabelSubTitle(
    text: String
) {
    Text(text, color = Black60,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        style = MaterialTheme.typography.titleLarge)
}

@Composable
fun Label(
    text: String,
    textAlign: TextAlign? = TextAlign.Start,
    color: Color = Black90,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
    style: TextStyle = MaterialTheme.typography.labelLarge
) {
    Text(text, color = color,
        textAlign = textAlign,
        modifier = modifier,
        style = style)
}

@Composable
fun TextField(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
    keyboard: KeyboardOptions,
    trailingIcon: @Composable (() -> Unit)? = null,
    @DrawableRes leadingIcon: Int? = null,
    text: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    isPassword: Boolean = false,
    label: String = emptyString(),
    supportingText: String = emptyString(),
    leadingIconColor: Color = Primary,
) {
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier,
        keyboardOptions  = keyboard,
        label = { Text(label, color = Gray30, style = MaterialTheme.typography.bodySmall) },
        isError = supportingText.isNotEmpty(),
        supportingText = {
            if(supportingText.isNotBlank()) {
                Text(supportingText, modifier = Modifier.padding(0.dp))
            }
        },
        leadingIcon = {
            leadingIcon?.let {
                Icon(
                    painter = painterResource(id = leadingIcon),
                    modifier = Modifier.size(18.dp),
                    contentDescription = stringResource(R.string.icon_text),
                    tint = leadingIconColor
                )
            }
        },
        trailingIcon = {
            if(isPassword) {
                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(
                        painter = painterResource(id = if(passwordVisibility) R.drawable.ic_eye else R.drawable.ic_eye_off),
                        modifier = Modifier.size(18.dp),
                        contentDescription = stringResource(R.string.toggle_text)
                    )
                }
            } else {
                trailingIcon?.invoke()
            }
        },
        singleLine = true,
        value = text,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(4.dp),
        visualTransformation =
            if(isPassword) {
                if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation()
              } else {
                VisualTransformation.None
             },
        colors = TextFieldDefaults.textFieldColors(
            textColor = Black90,
            disabledTextColor = Gray60,
            focusedIndicatorColor = BorderFocus,
            unfocusedIndicatorColor =BorderNormal,
            disabledIndicatorColor = Transparent,
            containerColor = White30,
            errorSupportingTextColor = Red90,
            errorIndicatorColor = Red90
        ),
        textStyle = MaterialTheme.typography.labelLarge
    )
}

@Composable
fun Button(
    modifier: Modifier = Modifier,
    text: String,
    action: () -> Unit,
    enabled: Boolean = true,
    color: Color = ButtonNormal
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(4.dp),
        onClick = action,
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = White30,
            disabledContainerColor = ButtonDisable,
            disabledContentColor = Gray30
        ),
        contentPadding = ButtonDefaults.TextButtonContentPadding
    ) {
        Text(text, style = buttonTextMedium)
    }
}

@Composable
fun Card() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 12.dp, bottom = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .padding(vertical = 4.dp, horizontal = 32.dp)
                .matchParentSize()
                .clip(RoundedCornerShape(10.dp))
                .background(Primary))

        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(vertical = 18.dp, horizontal = 18.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(10.dp))
                .background(Gray90)
                .paint(
                    painter = painterResource(R.drawable.bg_pattern),
                    contentScale = ContentScale.Crop
                )
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = "IDR7.500.000",
                    style = MaterialTheme.typography.headlineMedium,
                    color = White30
                )

                Text(text = "Saldo Aktif".uppercase(),
                    style = MaterialTheme.typography.titleSmall,
                    color = Primary
                )
            }

            FilledIconButton(
                onClick = { /* Do something! */ },
                shape = RoundedCornerShape(10.dp),
                colors = IconButtonDefaults.iconButtonColors(containerColor = Primary, contentColor = Black90)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_eye),
                    modifier = Modifier.size(18.dp),
                    contentDescription = "New Transactions")
            }

        }
    }
}

@Composable
fun AlertButtonCenteredDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    description: String
) {
    AlertDialog(
        containerColor = White30,
        titleContentColor = Black90,
        textContentColor = Black90,
        iconContentColor = Red90,
        onDismissRequest = onDismissRequest,
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_exclamation),
                modifier = Modifier.size(56.dp), contentDescription = null)
        },
        title = {
            Text(text = stringResource(R.string.oops_text))
        },
        text = {
            Text(
                description
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(stringResource(R.string.ok_text))
            }
        },
    )
}

@Composable
fun CircleButton(
    action: () -> Unit,
    @DrawableRes icon: Int,
) {
    Box(
        modifier = Modifier
            .clickable {
                action.invoke()
            }
            .border(2.dp, White30, RoundedCornerShape(40.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        buttonGradient1,
                        buttonGradient1,
                        buttonGradient2
                    )
                ), shape = RoundedCornerShape(40.dp)
            ).size(54.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            modifier = Modifier.size(18.dp),
            contentDescription = emptyString(),
            tint = White30
        )
    }
}

@Composable
fun <T: Any> rememberMutableStateListOf(vararg elements: T): SnapshotStateList<T> {
    return rememberSaveable(
        saver = listSaver(
            save = { stateList ->
                if (stateList.isNotEmpty()) {
                    val first = stateList.first()
                    if (!canBeSaved(first)) {
                        throw IllegalStateException("${first::class} cannot be saved. By default only types which can be stored in the Bundle class can be saved.")
                    }
                }
                stateList.toList()
            },
            restore = { it.toMutableStateList() }
        )
    ) {
        elements.toList().toMutableStateList()
    }
}

@Composable
fun LabelItem(label: String, content: String) {
    Row(modifier = Modifier.padding(top= 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Label(
            label,
            TextAlign.Start, Black60,
            modifier = Modifier.width(160.dp).padding(start = 26.dp),
            MaterialTheme.typography.bodyMedium)

        Label(
            ": $content",
            TextAlign.Start, Black30,
            modifier = Modifier.wrapContentWidth(),
            MaterialTheme.typography.labelLarge)
    }
}