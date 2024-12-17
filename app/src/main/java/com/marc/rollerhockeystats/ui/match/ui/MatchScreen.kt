package com.marc.rollerhockeystats.ui.match.ui

import android.annotation.SuppressLint
import android.app.Notification
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.marc.rollerhockeystats.R
import com.marc.rollerhockeystats.ui.viewmodel.MatchViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marc.rollerhockeystats.ui.models.Action
import com.marc.rollerhockeystats.ui.models.Player
import com.marc.rollerhockeystats.ui.models.ActionTypes
import com.marc.rollerhockeystats.ui.models.Circle
import com.marc.rollerhockeystats.ui.viewmodel.MatchViewModelFactory
import com.marc.rollerhockeystats.ui.viewmodel.MatchesViewModel


@Composable
fun MatchScreen(matchId : String, navController: NavController, matchesViewModel: MatchesViewModel){

    val matchViewModel : MatchViewModel = viewModel (factory = MatchViewModelFactory(matchId))

    var selectedPlayer by remember { mutableStateOf<Player?>(null) }
    var selectedAction by remember { mutableStateOf<String?>(null)}


    Scaffold(
        topBar = { MatchTopBar(matchViewModel,navController)},
        bottomBar = { BottomBar(selectedAction,
                        onActionSelected = { action -> selectedAction = action}                   )}

    ){ padding ->
        Row(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
        ) {
            HomeTeamBar(matchViewModel,
                selectedPlayer,
                onPlayerSelected = { player -> selectedPlayer = player }
            )
            HockeyRink(
                matchViewModel = matchViewModel,
                selectedPlayer = selectedPlayer,
                selectedAction = selectedAction,
                currentPart = 1
            ) //TODO: revisar
            AwayTeamBar(matchViewModel,
                selectedPlayer,
                onPlayerSelected = { player -> selectedPlayer = player }
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchTopBar(viewModel : MatchViewModel, navController: NavController){

    val timeLeft by viewModel.timeLeft.collectAsState()
    val timeRunning by viewModel.timeRunning.collectAsState()
    val currentHalf by viewModel.currentHalf.collectAsState()
    val match by viewModel.match.collectAsState()
    val halfs = match?.halfs

    CenterAlignedTopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()

            ) {
//                Text("FCB ${viewModel.homeScore} - ") //TODO: mostrar nom equips
//                Text("${viewModel.awayScore} Liceo")
                Text("FCB 3 - 1 Liceo")

            }
        },
        actions = {
            IconButton(onClick = { viewModel?.timeController() }) { //TODO: revisar
                Icon(
                    imageVector = if (timeRunning)
                        Icons.Filled.Pause
                    else
                        Icons.Filled.PlayArrow,
                    contentDescription = if (timeRunning)
                        "Pause"
                    else "Play"
                )
            }
            Text(timeLeft.formatAsMatchTime())


            if(halfs != null &&currentHalf < halfs){ //TODO: revisar
                Button(onClick = {
                    viewModel.setToNextHalf() //avancem de part
                    viewModel.resetTimeLeft() //reiniciem el temps
                }){
                    Text("Avançar a següent part")
                }
            }
            else{
                Button(onClick = {navController.navigate("home")}){
                    Text("Finalitzar partit")
                }
            }

        }
    )
}

@Composable
fun HomeTeamBar(viewModel : MatchViewModel, selectedPlayer : Player?, onPlayerSelected : (Player) -> Unit){
    val match by viewModel.match.collectAsState()
    val homeTeamPlayers = match?.homeTeam?.teamPlayers

    Column{
        if (homeTeamPlayers != null) {
            for(player in homeTeamPlayers){
                Button(
                    onClick = { onPlayerSelected(player) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                        if (player == selectedPlayer)
                            Color.Gray
                        else
                            Color.LightGray
                    )
                ){
                    Text(player.name)
                }
            }
        }
    }
}

@Composable
fun AwayTeamBar(viewModel : MatchViewModel, selectedPlayer : Player?, onPlayerSelected : (Player) -> Unit){
    val match by viewModel.match.collectAsState()
    val awayTeamPlayers = match?.homeTeam?.teamPlayers

    Column{
        if (awayTeamPlayers != null) {
            for(player in awayTeamPlayers){
                Button(
                    onClick = { onPlayerSelected(player) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                        if (player == selectedPlayer)
                            Color.Gray
                        else
                            Color.LightGray
                    )
                ){
                    Text(player.name)
                }
            }
        }
    }
}

@Composable
fun BottomBar(selectedAction : String?, onActionSelected : (String) -> Unit){
    //adjudiquem un color a cada acció

    val actions = listOf(
        ActionTypes.GOAL to Color.Magenta,
        ActionTypes.SHOT to Color.Gray,
        ActionTypes.SHOT_ON_GOAL to Color.Green,
        ActionTypes.ASSIST to Color.Cyan,
        ActionTypes.FOUL to Color.Yellow,
        ActionTypes.BLUE_CARD to Color.Blue,
        ActionTypes.RED_CARD to Color.Red
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ){
        for((action, color) in actions){
            Button(
                onClick = { onActionSelected(action) },
                colors = ButtonDefaults.buttonColors(containerColor = color),
                modifier = Modifier.weight(1f)
            ){
                Text(action, fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun HockeyRink(
    matchViewModel: MatchViewModel,
    selectedPlayer: Player?,
    selectedAction : String?,
    currentPart : Int,

){

    var circle by remember {mutableStateOf<Circle?>(null)}

    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.pistapartit),
            contentDescription = "Pista d'hoquei",
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        if (selectedPlayer != null && selectedAction != null) {
                            val action = Action(
                                player = selectedPlayer,
                                homeTeam = true,
                                actionType = selectedAction,
                                position = offset
                            )
                            matchViewModel.registerAction(action, currentPart)
                            circle = Circle(offset, action.actionType)
                        }
                    }
                }
        )
        Canvas(modifier = Modifier.fillMaxSize()){
            circle?.let {
                drawCircle(it.color, radius = 2f, center = it.position)
            }
        }
    }
}
    fun Int.formatAsMatchTime(): String {
        val minutes = this / 60
        val seconds = this % 60
        return String.format("%02d:%02d", minutes, seconds)
    }






