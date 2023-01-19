package com.ismealdi.visit.extension

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

// Navigate to the "page” destination only if we’re not already on
// the "page" destination, avoiding multiple copies on the top of the
// back stack
fun NavHostController.navigateSingleTopTo(route: String) {
    this.navigate(route) {
        /*popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }*/

        graph.label = route

        launchSingleTop = true
        restoreState = true
    }
}

fun NavHostController.navigateToAndBackTo(route: String, destination: String) {
    this.navigate(route) {
        popUpTo(destination) {
            saveState = true
        }

        graph.label = route

        launchSingleTop = true
        restoreState = true
    }
}

// Pop everything up to and including the "page" destination off
// the back stack before navigating to the "new page" destination
fun NavHostController.navigateInclusiveSingleTopTo(route: String) {
    this.navigate(route) {
        popUpTo(this@navigateInclusiveSingleTopTo.graph.findStartDestination().id) {
            inclusive = true
        }

        graph.label = route

        launchSingleTop = true
        restoreState = true
    }
}
