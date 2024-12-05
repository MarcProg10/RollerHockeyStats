package com.marc.rollerhockeystats.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marc.rollerhockeystats.ui.creatematch.ui.CreateMatchScreen
import com.marc.rollerhockeystats.ui.home.ui.HomeScreen
import com.marc.rollerhockeystats.ui.match.ui.MatchScreen
import com.marc.rollerhockeystats.ui.teamsRegister.ui.EnterAwayTeamScreen
import com.marc.rollerhockeystats.ui.teamsRegister.ui.EnterHomeTeamScreen
import com.marc.rollerhockeystats.ui.viewmodel.MatchViewModel

@Composable
fun MainNavHost(){

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ){
        composable("home") { HomeScreen(navController) }
        composable("createMatch"){ CreateMatchScreen(MatchViewModel(),navController) }
        composable("enterHomeTeam"){ EnterHomeTeamScreen(MatchViewModel(), navController)}
        composable("enterAwayTeam"){ EnterAwayTeamScreen(MatchViewModel(), navController)}
        composable("matchScreen"){ MatchScreen(MatchViewModel(), navController)}
    }
}