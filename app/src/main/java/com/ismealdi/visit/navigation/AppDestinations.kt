package com.ismealdi.visit.navigation

import com.ismealdi.visit.R
import com.ismealdi.visit.extension.emptyString
import com.ismealdi.visit.extension.firstPath
import com.ismealdi.visit.navigation.route.ConstantRoutes.HOME_VIEW
import com.ismealdi.visit.navigation.route.ConstantRoutes.LOGIN_VIEW
import com.ismealdi.visit.navigation.route.ConstantRoutes.SPLASH_VIEW
import com.ismealdi.visit.navigation.route.ConstantRoutes.STORE_VIEW
import com.ismealdi.visit.navigation.route.ConstantRoutes.SUMMARY_VIEW
import com.ismealdi.visit.navigation.route.ConstantRoutes.VISIT_VIEW

interface AppDestinations {
    val label: String
    val route: String
    val sublabel: String get() = emptyString()
    val icon: Int? get() = null
}

object Splash : AppDestinations {
    override val label = "Training ALdi"
    override val route = SPLASH_VIEW
}

object Login : AppDestinations {
    override val label = "Login"
    override val route = LOGIN_VIEW
}

object Home : AppDestinations {
    override val label = "Main Menu"
    override val route = HOME_VIEW
    override val icon = R.drawable.ic_refresh
}

object Visit : AppDestinations {
    override val label = "List Store"
    override val route = VISIT_VIEW
    override val sublabel = "MD000101"
    override val icon = R.drawable.ic_data
}

object Store : AppDestinations {
    override val label = "Verifikasi Store"
    override val route = STORE_VIEW
    override val sublabel = "MD000101"
}

object Summary : AppDestinations {
    override val label = "Main Menu"
    override val route = SUMMARY_VIEW
    override val sublabel = "userA"
    override val icon = R.drawable.ic_data
}

fun getTitleByRoute(route: String?): String {
    return when (route.firstPath()) {
        Home.route-> Home.label
        Login.route-> Login.label
        Splash.route-> Splash.label
        Visit.route-> Visit.label
        Store.route-> Store.label
        Summary.route-> Summary.label
        else -> emptyString()
    }
}

fun getSubTitleByRoute(route: String?): String {
    return when (route.firstPath()) {
        Visit.route-> Visit.sublabel
        Store.route-> Store.sublabel
        Summary.route-> Summary.sublabel
        else -> emptyString()
    }
}

fun isNoBar(route: String?) : Boolean  {
    return route.firstPath() == Login.route
}

fun noBack(route: String?) : Boolean  {
    return route.firstPath() == Summary.route
}

fun isWhiteBar(route: String?) : Boolean  {
    return route.firstPath() == Visit.route || route.firstPath() == Summary.route
}

fun icon(route: String?) : Int?  {
    return when (route.firstPath()) {
        Home.route-> Home.icon
        Visit.route-> Visit.icon
        Store.route-> Store.icon
        Summary.route-> Summary.icon
        else -> null
    }
}