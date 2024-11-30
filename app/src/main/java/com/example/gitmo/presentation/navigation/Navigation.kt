package com.example.gitmo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gitmo.presentation.RepoDetailScreen.RepoDetailScreen
import com.example.gitmo.presentation.homescreen.HomeScreen
import com.example.gitmo.presentation.viewmodels.MainViewModel

@Composable
fun Navigation(modifier: Modifier = Modifier , viewModel: MainViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.HomeScreen.route){
        composable(route = Screens.HomeScreen.route) {
            HomeScreen(viewModel = viewModel , navController = navController)
        }

        composable(
            route = Screens.RepositoryScreen.route + "/{ownerName}/{repoName}",
            arguments = listOf(
                navArgument("ownerName") { type = NavType.StringType },
                navArgument("repoName") { type = NavType.StringType }
            )
        ) {backStackEntry ->
            val ownerName = backStackEntry.arguments?.getString("ownerName")
            val repoName = backStackEntry.arguments?.getString("repoName")
            RepoDetailScreen(viewModel = viewModel, ownerName = ownerName.toString(), repoName = repoName.toString())
        }

    }

}