package com.example.foodslist.ui.pages

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.foodslist.R
import com.example.foodslist.navigation.NavigationItem
import com.example.foodslist.navigation.Route
import com.example.foodslist.ui.pages.about.AboutPage
import com.example.foodslist.ui.pages.details.DetailPage
import com.example.foodslist.ui.pages.favorites.FavoritesPage
import com.example.foodslist.ui.pages.home.HomePage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(
    modifier: Modifier = Modifier   ,
    navHostController: NavHostController = rememberNavController(),
){
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute==Route.Detail.route || currentRoute==Route.About.route) return@Scaffold
            BottomBar(navHostController = navHostController, modifier = modifier, currentRoute = currentRoute)
        }
    ) {padding->
        NavHost(navController = navHostController, startDestination = Route.Home.route, modifier = Modifier.padding(padding)){
            composable(Route.Home.route){
                HomePage(
                    detailNavigation = {foodId->
                        navHostController.navigate(Route.Detail.createRoute(foodId))
                    }
                )
            }
            composable(Route.Favorite.route){
                FavoritesPage(
                    detailNavigation = {foodId->
                        navHostController.navigate(Route.Detail.createRoute(foodId))
                    }
                )
            }
            composable(Route.About.route){
                AboutPage()
            }
            composable(Route.Detail.route, arguments = listOf(
                navArgument("foodId"){type = NavType.IntType}
            )){
                val id = it.arguments?.getInt("foodId")?:-1
                DetailPage(
                    foodId = id,

                )
            }
        }
    }
}

@Composable
private fun BottomBar(
    modifier: Modifier,
    navHostController: NavHostController,
    currentRoute:String?
){
    NavigationBar(
        modifier = modifier
    ) {
        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(R.string.menu_home),
                icon = Icons.Default.Home,
                route = Route.Home
            ),
            NavigationItem(
                title = stringResource(R.string.menu_fav),
                icon = Icons.Default.Favorite,
                route = Route.Favorite
            ),
            NavigationItem(
                title = stringResource(R.string.menu_about),
                icon = Icons.Default.AccountCircle,
                route = Route.About
            ))
        navigationItems.map {item->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = currentRoute == item.route.route,
                onClick = {
                    navHostController.navigate(item.route.route) {
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}