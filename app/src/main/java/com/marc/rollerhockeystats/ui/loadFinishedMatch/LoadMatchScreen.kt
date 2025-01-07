package com.marc.rollerhockeystats.ui.loadFinishedMatch

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.marc.rollerhockeystats.models.Match
import com.marc.rollerhockeystats.viewmodel.MatchesViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadMatchScreen(navController : NavController){
    val matchesViewModel : MatchesViewModel = viewModel()
    val matches = matchesViewModel.matches
    Log.d("LoadMatchScreen", "Partits a mostrar: $matches")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Visualitzar partit finalitzat") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Tornar enrere",
                            tint = Color.Black
                        )
                    }
                }, colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->

            Text(
                text = "Selecciona partit a visualitzar",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(12.dp)
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(64.dp)
        ) {
            if(matches.isEmpty()){
                item{
                    Text(
                        text = "No hi ha cap partit finalitzat",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            else{
                for(match in matches){
                    if(match.finished){
                        item{
                            MatchCard(match, navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MatchCard(match: Match, navController: NavController) {

    val date = match.selectedDate?.let { convertMillisToDate(it) } ?: "Data no especificada"
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { navController.navigate("matchStatsScreen/${match.id}") }
    ){
        Column(
            modifier = Modifier
                .padding(16.dp)
        ){
            Text(
                text = "${match.homeTeam?.teamName} ${match.homeScore} - ${match.awayScore} ${match.awayTeam?.teamName}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Categoria: ${match.category} | Minuts per part: ${match.minutes} | Ubicació del partit: ${match.ubication} | $date",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray

            )

        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}
