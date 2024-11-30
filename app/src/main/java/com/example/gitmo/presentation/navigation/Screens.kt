package com.example.gitmo.presentation.navigation

sealed class Screens(val route : String) {
    data object HomeScreen : Screens("homeScreen")
    data object RepositoryScreen : Screens("repoScreen")
    data object ContributersScreen : Screens("contributersScreen")
}