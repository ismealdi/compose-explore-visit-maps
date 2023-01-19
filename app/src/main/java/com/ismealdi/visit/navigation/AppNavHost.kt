package com.ismealdi.visit.navigation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ismealdi.visit.extension.navigateInclusiveSingleTopTo
import com.ismealdi.visit.extension.navigateSingleTopTo
import com.ismealdi.visit.extension.navigateToAndBackTo
import com.ismealdi.visit.view.ui.home.HomeView
import com.ismealdi.visit.view.ui.login.LoginView
import com.ismealdi.visit.view.ui.splash.SplashView
import com.ismealdi.visit.view.ui.store.StoreView
import com.ismealdi.visit.view.ui.summary.SummaryView
import com.ismealdi.visit.view.ui.visit.VisitView
import com.ismealdi.visit.viewmodel.SessionViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    loadingState: (Boolean) -> Unit = {}
) {

    val sessionViewModel : SessionViewModel = koinViewModel()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = (if(sessionViewModel.loggedIn()) Home.route else Login.route)
    ) {

        composable(
            route = Splash.route
        ) {
            SplashView(
                login = {
                    navController.navigateInclusiveSingleTopTo(Login.route)
                },
                home = {
                    navController.navigateInclusiveSingleTopTo(Home.route)
                }
            )
        }

        composable(
            route = Login.route
        ) {
            LoginView(
                onLoading = {
                    loadingState.invoke(it)
                },
                onLoginSuccess = {
                    it.updateState()
                    navController.navigateSingleTopTo(Home.route)
                }
            )
        }

        composable(
            route = Home.route
        ) {
            HomeView(
                onLoading = {
                    loadingState.invoke(it)
                },
                goToVisit = {
                    navController.navigateSingleTopTo(Visit.route)
                },
                onLogout = {
                    it.updateState()
                    navController.navigateSingleTopTo(Login.route)
                }
            )
        }

        composable(
            route = Visit.route
        ) {
            VisitView(
                onLoading = {
                    loadingState.invoke(it)
                },
                onDetailClick = { id ->
                    navController.navigateSingleTopTo(Store.route + "/" + id)
                }
            )
        }

        composable(
            route = Store.route + "/{storeId}",
            arguments = listOf(navArgument("storeId") { type = NavType.IntType })
        ) { backStackEntry ->
            StoreView(
                storeId = backStackEntry.arguments?.getInt("storeId"),
                onLoading = {
                    loadingState.invoke(it)
                },
                onDetailClick = {
                    navController.navigateToAndBackTo(Summary.route+ "/" + backStackEntry.arguments?.getInt("storeId"), Visit.route)
                }
            )
        }

        composable(
            route = Summary.route + "/{storeId}",
            arguments = listOf(navArgument("storeId") { type = NavType.IntType })
        ) { backStackEntry ->
            SummaryView(
                storeId = backStackEntry.arguments?.getInt("storeId"),
                onLoading = {
                    loadingState.invoke(it)
                },
                onFinishClick = {
                    navController.navigateUp()
                }
            )
        }

    }

}

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}