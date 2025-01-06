package com.marc.rollerhockeystats.ui.resumeMatch

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.marc.rollerhockeystats.ui.models.Match
import com.marc.rollerhockeystats.ui.viewmodel.MatchesViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumeMatchScreen(navController: NavController) {

    val matchesViewModel: MatchesViewModel = viewModel()
    val matches = matchesViewModel.matches
    Log.d("LoadMatchScreen", "Partits a mostrar: $matches")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reemprendre partit iniciat") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
            text = "Selecciona partit a reemprendre",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(paddingValues)
        )

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (matches.isEmpty()) {
                item {
                    Text(
                        text = "No hi ha cap partit a reemprendre",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                var count = 0
                for (match in matches) {
                    if (match.started && (!match.finished)) {
                        item {
                            ResumeMatchCard(match, navController)
                        }
                        count++
                    }
                }
                if (count == 0) {
                    item {
                        Text(
                            text = "No hi ha cap partit a reemprendre",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ResumeMatchCard(match: Match, navController: NavController) {
    val date = match.selectedDate?.let { convertMillisToDate(it) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { navController.navigate("matchScreen/${match.id}") }
    ){
        Column(
            modifier = Modifier
                .padding(16.dp)
        ){
            Text(
                text = "${match.homeTeam?.teamName} ${match.homeScore} - ${match.awayScore} ${match.awayTeam?.teamName}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Categoria: ${match.category} - ${match.ubication} - $date",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray

            )
            Text(
                text = "Part actual: ${match.currentHalf}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )

        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}



