package com.marc.rollerhockeystats.ui.MatchStats.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.marc.rollerhockeystats.ui.viewmodel.MatchViewModel
import com.marc.rollerhockeystats.ui.viewmodel.MatchViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchStatsScreen(matchId : String, navController : NavController) {

    Log.d("MatchScreen", "Creant instància MatchViewModel amb matchId: $matchId")
    val matchViewModel : MatchViewModel = viewModel (factory = MatchViewModelFactory(matchId))
    Log.d("MatchScreen", "MatchViewModel creat: $matchViewModel")
    val match by matchViewModel.match.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estadístiques globals del partit") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Tornar al menú principal",
                            tint = Color.Black
                        )
                    }
                },
                actions = @androidx.compose.runtime.Composable {
                    Button(
                        onClick = { navController.navigate(/* TODO */)},
                        modifier = Modifier.padding(end = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.Black
                        )
                    ){
                        Text("Estadístiques individuals")
                    }
                },

                colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .wrapContentSize(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "RESUM DEL PARTIT",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.wrapContentSize(Alignment.TopStart)
            )
            Spacer(Modifier.height(20.dp))

            Text(
                text = "${match?.homeTeam?.teamName} - ${match?.awayTeam?.teamName}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))

            Text(
                text = "GOLS",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text("${match?.homeScore} - ${match?.awayScore}")
            Spacer(Modifier.height(8.dp))

            Text(
                text = "TIRS",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text("${match?.homeTeam?.totalShots()} - ${match?.awayTeam?.totalShots()}")
            Spacer(Modifier.height(8.dp))

            Text(
                text = "TIRS A PORTA",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text("${match?.homeTeam?.totalShotsOnGoal()} - ${match?.awayTeam?.totalShotsOnGoal()}")
            Spacer(Modifier.height(8.dp))

            Text(
                text = "ASSISTÈNCIES",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text("${match?.homeTeam?.totalAssists()} - ${match?.awayTeam?.totalAssists()}")
            Spacer(Modifier.height(8.dp))

            Text(
                text = "FALTES",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text("${match?.homeFouls} - ${match?.awayFouls}")
            Spacer(Modifier.height(8.dp))

            Text(
                text = "TARGETES BLAVES",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text("${match?.homeTeam?.totalBlueCards()} - ${match?.awayTeam?.totalBlueCards()}")
            Spacer(Modifier.height(8.dp))

            Text(
                text = "TARGETES VERMELLES",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text("${match?.homeTeam?.totalRedCards()} - ${match?.awayTeam?.totalRedCards()}")
            Spacer(Modifier.height(8.dp))

        }
    }
}

