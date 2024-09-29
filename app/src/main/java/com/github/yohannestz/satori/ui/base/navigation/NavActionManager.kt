package com.github.yohannestz.satori.ui.base.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Immutable
class NavActionManager(
    private val navController: NavHostController
) {
    fun navigateTo(route: String) {
        navController.navigate(route)
    }

    fun navigateUp() {
        navController.navigateUp()
    }

    fun goBack() {
        navController.popBackStack()
    }

    fun goBack(route: String) {
        navController.popBackStack(route, false)
    }

    companion object {
        @Composable
        fun rememberNavActionManager(
            navController: NavHostController = rememberNavController()
        ) = remember {
            NavActionManager(navController)
        }
    }
}