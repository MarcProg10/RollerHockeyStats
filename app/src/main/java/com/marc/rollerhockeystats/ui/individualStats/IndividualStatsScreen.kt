package com.marc.rollerhockeystats.ui.individualStats

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.marc.rollerhockeystats.R
import com.marc.rollerhockeystats.ui.models.Action
import com.marc.rollerhockeystats.ui.models.Player
import com.marc.rollerhockeystats.ui.viewmodel.MatchViewModel
import com.marc.rollerhockeystats.ui.viewmodel.MatchViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndividualStatsScreen(matchId : String, navController : NavController) {

    Log.d("MatchScreen", "Creant instància MatchViewModel amb matchId: $matchId")
    val matchViewModel: MatchViewModel = viewModel(factory = MatchViewModelFactory(matchId))
    Log.d("MatchScreen", "MatchViewModel creat: $matchViewModel")
    val match by matchViewModel.match.collectAsState()
    var homeExpanded by remember { mutableStateOf(false) }
    var awayExpanded by remember { mutableStateOf(false) }
    var selectedPlayer by remember { mutableStateOf<Player?>(null) }
    val isPrebenjami = match?.isPrebenjami()
    val rinkHockeyWidth = match?.rinkWidth
    val rinkHockeyHeight = match?.rinkHeight




    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estadístiques individuals") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Tornar al menú principal",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->

        Row{
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .wrapContentSize(Alignment.Center)
                    .background(Color.Blue),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {

                Text(
                    text = "1: Selecciona plantilla",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.wrapContentSize(Alignment.TopStart)
                )

                Text(
                    text = "2: Selecciona jugador/a",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.wrapContentSize(Alignment.TopStart)
                )

                Row {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = { homeExpanded = !homeExpanded }) {
                            Text(
                                text = "${match?.homeTeam?.teamName}",
                            )
                        }
                        DropdownMenu(
                            expanded = homeExpanded,
                            onDismissRequest = { homeExpanded = false }
                        ) {

                            for (player in match?.homeTeam?.teamPlayers!!) {
                                DropdownMenuItem(
                                    text = { Text("${player.name} : ${player.number}") },
                                    onClick = { selectedPlayer = player }
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .padding(16.dp)

                    ) {
                        OutlinedButton(
                            onClick = { awayExpanded = !awayExpanded }) {
                            Text(
                                text = "${match?.awayTeam?.teamName}",
                            )
                            DropdownMenu(
                                expanded = awayExpanded,
                                onDismissRequest = { awayExpanded = false }
                            ) {

                                for (player in match?.awayTeam?.teamPlayers!!) {
                                    DropdownMenuItem(
                                        text = { Text("${player.name} : ${player.number}") },
                                        onClick = { selectedPlayer = player }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .padding(1.dp),

                    ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "RESUM INDIVIDUAL DEL PARTIT",
                            fontWeight = FontWeight.ExtraBold
                        )

                        if (selectedPlayer != null) {
                            if (selectedPlayer!!.ishome) {
                                Text(
                                    text = "${match?.homeTeam?.teamName}",
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Text(
                                    text = "${match?.awayTeam?.teamName}",
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = "${selectedPlayer?.name} - Dorsal: ${selectedPlayer?.number}",
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(Modifier.height(8.dp))
                            Text("Gols: ${selectedPlayer?.goals}")
                            Spacer(Modifier.height(8.dp))
                            Text("Tirs: ${selectedPlayer?.shots}")
                            Spacer(Modifier.height(8.dp))
                            Text("Tirs a porta: ${selectedPlayer?.shotsOnGoal}")
                            Spacer(Modifier.height(8.dp))
                            Text("Assistències: ${selectedPlayer?.assists}")
                            Spacer(Modifier.height(8.dp))
                            Text("Faltes: ${selectedPlayer?.fouls}")
                            Spacer(Modifier.height(8.dp))
                            Text("Targetes blaves: ${selectedPlayer?.blueCards}")
                            Spacer(Modifier.height(8.dp))
                            Text("Targetes vermelles: ${selectedPlayer?.redCards}")
                        } else
                            Text("Selecciona un jugador/a per veure les dades")
                    }
                }

            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .wrapContentSize(Alignment.Center)
                    .background(Color.Blue),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Text(
                    text = "Accions primera part",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )

                Box(modifier = Modifier){ //pintem a la pista de la 1a part (1-2 prebenjamí)
                    Image(
                        painter = painterResource(id = R.drawable.pistapartit),
                        contentDescription = "Pista d'hoquei",
                        modifier = Modifier.fillMaxSize()
                    )
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(0.dp)
                    ){
                        matchViewModel.updateStatsScreen1Width(size.width)
                        matchViewModel.updateStatsScreen1Height(size.height)
                        Log.d("IndividualStatsScreen", "Width: ${size.width}, Height: ${size.height}")
                        if(selectedPlayer != null && isPrebenjami != null){
                           drawPlayerActions(selectedPlayer!!, 1, isPrebenjami, matchViewModel)
                        }
                    }
                }

//                Box(modifier = Modifier){ //pintem a la pista de la 2a part (3-4 prebenjamí)
//                    Image(
//                        painter = painterResource(id = R.drawable.pistapartit),
//                        contentDescription = "Pista d'hoquei",
//                        modifier = Modifier.fillMaxSize()
//                    )
//                    Canvas(modifier = Modifier.fillMaxSize()){
//
//                        matchViewModel.statsScreen2Width = size.width
//                        matchViewModel.statsScreen2Height = size.height
//                        if(selectedPlayer != null && isPrebenjami != null){
//                            drawPlayerActions(selectedPlayer!!, 2, isPrebenjami, matchViewModel)
//                        }
//                    }
//                }
            }
         }
    }
}


private fun DrawScope.drawPlayerActions(player : Player, halfToDraw : Int, isPrebenjami : Boolean, matchViewModel : MatchViewModel){


    if(halfToDraw == 1) {
        for (action in player.firstHalfActions)
            drawAction(action, matchViewModel)
        if (isPrebenjami) {
            for (action in player.secondHalfActions)
                drawAction(action, matchViewModel)
        }
    }
    else{
        if(! isPrebenjami)
            for(action in player.secondHalfActions)
                drawAction(action, matchViewModel)
        else{
            for(action in player.thirdHalfActions)
                drawAction(action, matchViewModel)
            for(action in player.fourthHalfActions)
                drawAction(action, matchViewModel)
        }
    }
}

private fun DrawScope.drawAction(action : Action, matchViewModel: MatchViewModel){
    val color = when(action.actionType){
        "Gol" -> Color.Magenta
        "Tir" -> Color.Gray
        "T.porta" -> Color.Green
        "Assist" -> Color.Cyan
        "Falta" -> Color.Yellow
        "Vermella" -> Color.Red
        "Blava" -> Color.Blue
        else -> Color.Black
    }
    action.position?.let { drawCircle(color, radius = 14f, center = scalateOffset(it.toOffset(),matchViewModel))}

}

fun scalateOffset(offset : Offset, matchViewModel : MatchViewModel) : Offset{

    val rinkHockeyWidth = matchViewModel.match.value?.rinkWidth
    val rinkHockeyHeight = matchViewModel.match.value?.rinkHeight

    Log.d("IndividualStatsScreen", "RinkHockeyWidth: ${rinkHockeyWidth}, RinkHockeyHeight: ${rinkHockeyHeight}")
    Log.d("IndividualStatsScreen", "StatsScreen1Width: ${matchViewModel.statsScreen1Width.value}, StatsScreen1Height: ${matchViewModel.statsScreen1Height.value}")

    val newX = (offset.x / rinkHockeyWidth!!) * matchViewModel.statsScreen1Width.value
    val newY = (offset.y / rinkHockeyHeight!!) * matchViewModel.statsScreen1Height.value

    val newOffset = Offset(newX,newY)
    Log.d("IndividualStatsScreen", "Escalat de l'offset: $offset a $newOffset")
    return newOffset
}
