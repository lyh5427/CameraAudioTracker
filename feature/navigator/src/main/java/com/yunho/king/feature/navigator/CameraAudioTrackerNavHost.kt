package com.yunho.king.feature.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yunho.king.feature.appdetail.AppDetailScreen
import com.yunho.king.feature.intercept.audio.AudioInterceptScreen
import com.yunho.king.feature.intercept.camera.CameraInterceptScreen
import com.yunho.king.feature.launch.intro.IntroScreen
import com.yunho.king.feature.launch.perm.PermScreen
import com.yunho.king.feature.main.MainScreen
import com.yunho.king.feature.main.settings.SettingsScreen

@Composable
fun CameraAudioTrackerNavHost(
    navigatorViewModel: NavigatorViewModel
) {
    val navController = rememberNavController()
    val pendingRoute by navigatorViewModel.pendingRoute.collectAsState()

    LaunchedEffect(pendingRoute) {
        val route = pendingRoute ?: return@LaunchedEffect
        navController.navigate(route) {
            launchSingleTop = true
        }
        navigatorViewModel.consumePendingRoute()
    }

    NavHost(navController = navController, startDestination = "intro") {
        composable("intro") {
            IntroScreen(
                onNavigateToPerm = { navController.navigate("perm") },
                onNavigateToMain = {
                    navController.navigate("main") {
                        popUpTo("intro") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable("perm") {
            PermScreen(onNavigateToMain = {
                navController.navigate("main") {
                    popUpTo("intro") { inclusive = true }
                    launchSingleTop = true
                }
            })
        }
        composable("main") {
            MainScreen(
                onNavigateToAppDetail = { pkgName ->
                    navController.navigate("appdetail/$pkgName")
                },
                onNavigateToSettings = {
                    navController.navigate("settings")
                }
            )
        }
        composable("settings") {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
        composable(
            route = "appdetail/{pkgName}",
            arguments = listOf(navArgument("pkgName") { type = NavType.StringType })
        ) { backStackEntry ->
            val pkgName = backStackEntry.arguments?.getString("pkgName") ?: return@composable
            AppDetailScreen(
                pkgName = pkgName,
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "camera_intercept/{pkgName}",
            arguments = listOf(navArgument("pkgName") { type = NavType.StringType })
        ) { backStackEntry ->
            val pkgName = backStackEntry.arguments?.getString("pkgName") ?: return@composable
            CameraInterceptScreen(
                pkgName = pkgName,
                onDismiss = { navController.popBackStack() }
            )
        }
        composable(
            route = "audio_intercept/{pkgName}",
            arguments = listOf(navArgument("pkgName") { type = NavType.StringType })
        ) { backStackEntry ->
            val pkgName = backStackEntry.arguments?.getString("pkgName") ?: return@composable
            AudioInterceptScreen(
                pkgName = pkgName,
                onDismiss = { navController.popBackStack() }
            )
        }
    }
}
