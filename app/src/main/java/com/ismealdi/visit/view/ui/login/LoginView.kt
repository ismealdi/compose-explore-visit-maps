package com.ismealdi.visit.view.ui.loginimport androidx.compose.foundation.Imageimport androidx.compose.foundation.layout.*import androidx.compose.foundation.lazy.LazyColumnimport androidx.compose.foundation.selection.toggleableimport androidx.compose.foundation.text.KeyboardOptionsimport androidx.compose.material.icons.Iconsimport androidx.compose.material.icons.filled.Clearimport androidx.compose.material3.*import androidx.compose.runtime.Composableimport androidx.compose.runtime.getValueimport androidx.compose.runtime.livedata.observeAsStateimport androidx.compose.runtime.mutableStateOfimport androidx.compose.runtime.saveable.rememberSaveableimport androidx.compose.runtime.setValueimport androidx.compose.ui.Alignmentimport androidx.compose.ui.Modifierimport androidx.compose.ui.graphics.graphicsLayerimport androidx.compose.ui.layout.ContentScaleimport androidx.compose.ui.res.painterResourceimport androidx.compose.ui.res.stringResourceimport androidx.compose.ui.semantics.Roleimport androidx.compose.ui.text.input.ImeActionimport androidx.compose.ui.text.input.KeyboardTypeimport androidx.compose.ui.text.input.TextFieldValueimport androidx.compose.ui.text.style.TextAlignimport androidx.compose.ui.tooling.preview.Devicesimport androidx.compose.ui.tooling.preview.Previewimport androidx.compose.ui.unit.dpimport androidx.constraintlayout.compose.ConstraintLayoutimport com.ismealdi.visit.Rimport com.ismealdi.visit.extension.emptyStringimport com.ismealdi.visit.view.theme.*import com.ismealdi.visit.view.ui.components.CustomDialogimport com.ismealdi.visit.viewmodel.LoginViewModelimport org.koin.androidx.compose.koinViewModel@Preview(    name = "Preview",    device = Devices.PIXEL_2,    showBackground = true,    backgroundColor = 0xFFFFFFFF,    showSystemUi = true)@Composablefun LoginView(    onLoading: (Boolean) -> Unit = {},    onLoginSuccess: () -> Unit = {},) {    val loginViewModel : LoginViewModel = koinViewModel()    var email by rememberSaveable(stateSaver = TextFieldValue.Saver) {        mutableStateOf( TextFieldValue(emptyString()) )    }    var password by rememberSaveable(stateSaver = TextFieldValue.Saver) {        mutableStateOf(TextFieldValue(emptyString()))    }    var checkedState by rememberSaveable { mutableStateOf(false) }    val errorEvent by loginViewModel.errorEvent.observeAsState()    val loginEvent by loginViewModel.loginEvent.observeAsState()    val loadingEvent by loginViewModel.loadingEvent.observeAsState()    val rememberUsername by loginViewModel.rememberUsername.observeAsState()    errorEvent?.getEventIfNotHandled()?.let { error ->        if(error.state){            CustomDialog(                description = error.message,                onDismissRequest = {                    loginViewModel.closeErrorEvent()                },                onConfirm = {                    loginViewModel.closeErrorEvent()                }            )        }    }    loginEvent?.getEventIfNotHandled()?.let { data ->        onLoginSuccess.invoke()    }    loadingEvent?.getEventIfNotHandled()?.let { data ->        onLoading.invoke(data)    }    rememberUsername?.getEventIfNotHandled()?.let { data ->        checkedState = data.state        if(email.text.isEmpty())            email = TextFieldValue(data.message)    }    ConstraintLayout(        modifier = Modifier.fillMaxSize(),    ) {        val (topWave, contentBox, bottomWave, labelBox) = createRefs()        Image(            painter = painterResource(id = R.drawable.bg_blue_wave),            contentDescription = null, // decorative            alignment = Alignment.TopCenter,            contentScale = ContentScale.Fit,            modifier = Modifier                .fillMaxWidth()                .graphicsLayer {                    rotationZ = 180f                }                .constrainAs(topWave) {                    top.linkTo(parent.top)                    start.linkTo(parent.start)                    end.linkTo(parent.end)                },            )        Image(            painter = painterResource(id = R.drawable.bg_blue_wave),            contentDescription = null, // decorative            alignment = Alignment.TopCenter,            contentScale = ContentScale.Fit,            modifier = Modifier                .fillMaxWidth()                .constrainAs(bottomWave) {                    bottom.linkTo(parent.bottom)                    start.linkTo(parent.start)                    end.linkTo(parent.end)                }        )        LazyColumn(            verticalArrangement = Arrangement.spacedBy(4.dp),            modifier = Modifier                .padding(horizontal = 18.dp, vertical = 16.dp)                .constrainAs(contentBox) {                    top.linkTo(parent.top)                    bottom.linkTo(parent.bottom)                    start.linkTo(parent.start)                    end.linkTo(parent.end)                }        ) {            item {                Column(                    horizontalAlignment = Alignment.Start,                    verticalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.Top)                ) {                    Spacer(modifier = Modifier.size(24.dp))                    TextField(                        text = email,                        onValueChange = {                            email = it                        },                        label = "Username",                        keyboard = KeyboardOptions(                            keyboardType = KeyboardType.Email,                            imeAction = ImeAction.Next                        ),                        leadingIcon = R.drawable.ic_person_circle,                        trailingIcon = {                            if(!email.text.isNullOrEmpty()) {                                IconButton(onClick = {                                    email = TextFieldValue(emptyString())                                }) {                                    Icon(                                        Icons.Default.Clear,                                        modifier = Modifier.size(18.dp),                                        contentDescription = stringResource(R.string.clear_text)                                    )                                }                            }                        }                    )                    Spacer(modifier = Modifier.size(2.dp))                    TextField(                        text = password,                        isPassword = true,                        label = "Password",                        leadingIcon = R.drawable.ic_lock,                        onValueChange = {                            password = it                        },                        keyboard = KeyboardOptions(                            keyboardType = KeyboardType.Password,                            imeAction = ImeAction.Done                        )                    )                    Row(                        Modifier                            .fillMaxWidth()                            .wrapContentHeight(),                        horizontalArrangement = Arrangement.SpaceBetween,                        verticalAlignment = Alignment.CenterVertically) {                        Row(                            Modifier                                .width(120.dp)                                .toggleable(                                    value = checkedState,                                    onValueChange = { loginViewModel.toggleRememberUsername() },                                    role = Role.Checkbox                                ),                            verticalAlignment = Alignment.CenterVertically                        ) {                            Checkbox(                                checked = checkedState,                                colors = CheckboxDefaults.colors(                                    checkedColor = Primary,                                    uncheckedColor = Primary,                                    checkmarkColor = White30                                ),                                onCheckedChange = null                            )                            Label(stringResource(R.string.keep_username_text), TextAlign.Start, Primary,                                modifier = Modifier                                    .padding(start = 8.dp)                                    .wrapContentWidth(), MaterialTheme.typography.titleSmall)                        }                        TextButton(                            modifier = Modifier.wrapContentWidth(),                            onClick = { /* Do something! */ }) {                            Icon(                                painter = painterResource(id = R.drawable.ic_download),                                modifier = Modifier.size(16.dp),                                contentDescription = stringResource(R.string.check_update_text)                            )                            Text(text = stringResource(R.string.check_update_text),                                textAlign = TextAlign.End,                                color = Primary,                                modifier = Modifier.padding(start = 8.dp),                                style = MaterialTheme.typography.titleSmall)                        }                    }                    Spacer(modifier = Modifier.size(24.dp))                    Button(                        modifier = Modifier.fillMaxWidth(),                        text = stringResource(R.string.login_text),                        enabled = (email.text.isNotEmpty() && password.text.isNotEmpty()),                        action = {                            loginViewModel.performLogin(email.text, password.text)                        })                    Spacer(modifier = Modifier.size(44.dp))                    Label("App Ver 1.0.0 - 20013FEA6BCC820C", TextAlign.Center, Gray60,                        modifier = Modifier                            .fillMaxWidth(), MaterialTheme.typography.labelMedium)                }            }        }    }}