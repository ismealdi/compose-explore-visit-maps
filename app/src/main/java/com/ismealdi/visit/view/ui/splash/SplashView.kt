package com.ismealdi.visit.view.ui.splash

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ismealdi.visit.managers.SessionManager
import com.ismealdi.visit.viewmodel.SessionViewModel
import org.koin.androidx.compose.get
import org.koin.androidx.compose.koinViewModel

@Preview
@Composable
fun SplashView(
    login: () -> Unit = {},
    home: () -> Unit = {}
) {
    val sessionViewModel : SessionViewModel = koinViewModel()

    (if(sessionViewModel.isLogin.value == true) home else login).invoke()
}