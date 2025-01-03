package com.marc.rollerhockeystats.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.marc.rollerhockeystats.ui.MatchStats.ui.MatchStatsScreen
import com.marc.rollerhockeystats.ui.creatematch.ui.CreateMatchScreen
import com.marc.rollerhockeystats.ui.home.ui.HomeScreen
import com.marc.rollerhockeystats.ui.match.ui.MatchScreen
import com.marc.rollerhockeystats.ui.viewmodel.MatchesViewModel
import com.marc.rollerhockeystats.ui.teamsRegister.ui.EnterAwayTeamScreen
import com.marc.rollerhockeystats.ui.teamsRegister.ui.EnterHomeTeamScreen
import com.marc.rollerhockeystats.ui.viewmodel.MatchViewModel

@Composable
fun MainNavHost(matchesViewModel : MatchesViewModel){

        val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ){
        composable("home") { HomeScreen(navController)}
        composable("createMatch"){
            CreateMatchScreen(matchesViewModel,navController)
        }
        composable("enterHomeTeam/{matchId}",
            arguments = listOf(navArgument("matchId"){})){ backStackEntry ->
            val matchId = requireNotNull(backStackEntry.arguments?.getString("matchId"))
            EnterHomeTeamScreen(matchId, navController, matchesViewModel)
        }
        composable("enterAwayTeam/{matchId}",
            arguments = listOf(navArgument("matchId"){})){ backStackEntry ->
            val matchId = requireNotNull(backStackEntry.arguments?.getString("matchId"))
            EnterAwayTeamScreen(matchId, navController, matchesViewModel)
        }
        composable("matchScreen/{matchId}",
            arguments = listOf(navArgument("matchId"){})){ backStackEntry ->
            val matchId = requireNotNull(backStackEntry.arguments?.getString("matchId"))
            MatchScreen(matchId, navController, matchesViewModel)
        }
        composable("matchStatsScreen/{matchId}",
            arguments = listOf(navArgument("matchId"){})){ backStackEntry ->
            val matchId = requireNotNull(backStackEntry.arguments?.getString("matchId"))
            MatchStatsScreen(matchId, navController)
        }


    }
}