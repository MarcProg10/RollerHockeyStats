package com.marc.rollerhockeystats.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.marc.rollerhockeystats.ui.MatchStats.ui.MatchStatsScreen
import com.marc.rollerhockeystats.ui.creatematch.ui.CreateMatchScreen
import com.marc.rollerhockeystats.ui.home.ui.HomeScreen
import com.marc.rollerhockeystats.ui.individualStats.IndividualStatsScreen
import com.marc.rollerhockeystats.ui.loadFinishedMatch.LoadMatchScreen
import com.marc.rollerhockeystats.ui.match.ui.MatchScreen
import com.marc.rollerhockeystats.ui.resumeMatch.ResumeMatchScreen
import com.marc.rollerhockeystats.viewmodel.MatchesViewModel
import com.marc.rollerhockeystats.ui.teamsRegister.ui.EnterAwayTeamScreen
import com.marc.rollerhockeystats.ui.teamsRegister.ui.EnterHomeTeamScreen

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
            val homeScore = backStackEntry.arguments?.getString("homeScore")?.toIntOrNull() ?: -1
            val awayScore = backStackEntry.arguments?.getString("awayScore")?.toIntOrNull() ?: -1
            MatchScreen(matchId, navController, matchesViewModel, homeScore, awayScore)
        }
        composable("matchStatsScreen/{matchId}",
            arguments = listOf(navArgument("matchId"){})){ backStackEntry ->
            val matchId = requireNotNull(backStackEntry.arguments?.getString("matchId"))
            MatchStatsScreen(matchId, navController)
        }

        composable("individualStatsScreen/{matchId}",
            arguments = listOf(navArgument("matchId"){})){ backStackEntry ->
            val matchId = requireNotNull(backStackEntry.arguments?.getString("matchId"))
            IndividualStatsScreen(matchId, navController)
        }

        composable("loadFinishedMatch"){
            LoadMatchScreen(navController)
        }

        composable("resumeMatch") {
            ResumeMatchScreen(navController)
        }

        composable(
            "matchScreen/{matchId}/{homeScore}/{awayScore}",
            arguments = listOf(
                navArgument("matchId") {},
                navArgument("homeScore") {},
                navArgument("awayScore") {}
            )
        ) { backStackEntry ->
            val matchId = requireNotNull(backStackEntry.arguments?.getString("matchId"))
            val homeScore = requireNotNull(backStackEntry.arguments?.getString("homeScore")).toInt()
            val awayScore = requireNotNull(backStackEntry.arguments?.getString("awayScore")).toInt()
            MatchScreen(matchId, navController, matchesViewModel, homeScore, awayScore)
        }


    }
}