package com.github.yohannestz.satori.ui.main.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.github.yohannestz.satori.R
import com.github.yohannestz.satori.ui.base.BottomDestination
import com.github.yohannestz.satori.ui.base.BottomDestination.Companion.Icon

@Composable
fun MainNavigationRail(
    navController: NavController,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    NavigationRail(
        modifier = modifier,
        header = {
            FloatingActionButton(
                onClick = {
                    onItemSelected(-1)
                    navController.navigate("") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_round_search_24),
                    contentDescription = "search"
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Bottom
        ) {
            BottomDestination.railValues.forEachIndexed { index, dest ->
                val isSelected = navBackStackEntry?.destination?.hierarchy?.any {
                    it.hasRoute(dest.route::class)
                } == true
                NavigationRailItem(
                    selected = isSelected,
                    onClick = {
                        onItemSelected(index)
                        navController.navigate(dest.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { dest.Icon(selected = isSelected) },
                    label = { Text(text = stringResource(dest.title)) }
                )
            }
        }
    }
}