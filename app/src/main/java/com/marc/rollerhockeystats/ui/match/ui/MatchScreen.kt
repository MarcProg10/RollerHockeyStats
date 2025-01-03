package com.marc.rollerhockeystats.ui.match.ui

import android.annotation.SuppressLint
import android.app.Notification
import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marc.rollerhockeystats.ui.models.Action
import com.marc.rollerhockeystats.ui.models.Player
import com.marc.rollerhockeystats.ui.models.ActionTypes
import com.marc.rollerhockeystats.ui.models.Circle
import com.marc.rollerhockeystats.ui.viewmodel.MatchViewModelFactory
import com.marc.rollerhockeystats.ui.viewmodel.MatchesViewModel


@Composable
fun MatchScreen(matchId : String, navController: NavController, matchesViewModel: MatchesViewModel){

    Log.d("MatchScreen", "Creant instància MatchViewModel amb matchId: $matchId")
    val matchViewModel : MatchViewModel = viewModel (factory = MatchViewModelFactory(matchId))
    Log.d("MatchScreen", "MatchViewModel creat: $matchViewModel")

    var selectedPlayer by remember { mutableStateOf<Player?>(null) }
    var selectedAction by remember { mutableStateOf<String?>(null)}
    val actionColorMap = mapOf(
        ActionTypes.GOAL to Color.Magenta,
        ActionTypes.SHOT to Color.Gray,
        ActionTypes.SHOT_ON_GOAL to Color.Green,
        ActionTypes.ASSIST to Color.Cyan,
        ActionTypes.FOUL to Color.Yellow,
        ActionTypes.BLUE_CARD to Color.Blue,
        ActionTypes.RED_CARD to Color.Red
    )


    Scaffold(
        topBar = { MatchTopBar(matchViewModel,navController, matchId)},
        bottomBar = { BottomBar(
                        selectedAction = selectedAction,
                        onActionSelected = { action ->
                            selectedAction = action
                            Log.d("MatchScreen", "Nova acció seleccionada: $selectedAction")
                        },
                        actionColorMap
        )}


    ){ padding ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val (homeTeamBar, hockeyRink, awayTeamBar) = createRefs()

            HomeTeamBar(
                viewModel = matchViewModel,
                selectedPlayer = selectedPlayer,
                onPlayerSelected = { player ->
                    selectedPlayer = player
                    Log.d("MatchScreen", "Nou jugador seleccionat (home): $selectedPlayer")
                },
                modifier = Modifier
                    .background(Color.Red.copy(alpha = 0.5f))
                    .constrainAs(homeTeamBar) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(hockeyRink.start)
                        width = Dimension.value(80.dp)
                    }
                    .fillMaxHeight()
                    .zIndex(0.3f)
                    //.width(80.dp)
            )

            HockeyRink(
                matchViewModel = matchViewModel,
                selectedPlayer = selectedPlayer,
                selectedAction = selectedAction,
                currentPart = 1,
                modifier= Modifier
                    .background(Color.Green.copy(alpha = 0.5f))
                    .constrainAs(hockeyRink) {
                        start.linkTo(homeTeamBar.end)
                        end.linkTo(awayTeamBar.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .fillMaxHeight(),
                actionColorMap
            )

            AwayTeamBar(
                viewModel = matchViewModel,
                selectedPlayer = selectedPlayer,
                onPlayerSelected = { player ->
                    selectedPlayer = player
                    Log.d("MatchScreen", "Nou jugador seleccionat (away): $selectedPlayer")
                },
                modifier = Modifier
                    .background(Color.Blue.copy(alpha = 0.5f))
                    .constrainAs(awayTeamBar) {
                        start.linkTo(hockeyRink.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        width = Dimension.value(80.dp)
                    }
                    .fillMaxHeight()
                    //.width(80.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchTopBar(viewModel : MatchViewModel, navController: NavController, matchId : String){

    val timeLeft by viewModel.timeLeft.collectAsState()
    val timeRunning by viewModel.timeRunning.collectAsState()
    val currentHalf by viewModel.currentHalf.collectAsState()
    val match by viewModel.match.collectAsState()
    val halfs = match?.halfs

    CenterAlignedTopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()

            ) {

                Row(verticalAlignment = Alignment.CenterVertically){

                    IconButton(onClick = { viewModel.timeController() }) { //TODO: revisar
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

                    //Text("Part actual")
                    Text(timeLeft.formatAsMatchTime()) //TODO: revisar funcionament
                }

                Text("${match?.homeTeam?.teamName} ${match?.homeScore} - ${match?.awayScore} ${match?.awayTeam?.teamName}") //TODO: revisar reactivitat

                if(halfs != null && currentHalf < halfs){ //TODO: revisar
                    Button(onClick = {
                        viewModel.setToNextHalf() //avancem de part
                        viewModel.resetTimeLeft() //reiniciem el temps
                        viewModel.saveMatchToFirebase()
                    }){
                        Text("Avançar a següent part")
                    }
                }
                else{
                    Button(onClick = {
                        navController.navigate("matchStatsScreen/${matchId}")
                    }){
                        Text("Finalitzar partit")
                    }
                }
            }
        })
}

@Composable
fun HomeTeamBar(viewModel : MatchViewModel, selectedPlayer : Player?, onPlayerSelected : (Player) -> Unit, modifier : Modifier = Modifier){
    val match by viewModel.match.collectAsState()
    val homeTeamPlayers = match?.homeTeam?.teamPlayers

    Column(
        modifier = modifier
    ){
        Text("LOCAL")
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
                    Text("${player.number}")
                }
            }
        }
    }
}

@Composable
fun AwayTeamBar(viewModel : MatchViewModel, selectedPlayer : Player?, onPlayerSelected : (Player) -> Unit, modifier : Modifier = Modifier){
    val match by viewModel.match.collectAsState()
    val awayTeamPlayers = match?.awayTeam?.teamPlayers

    Column(
        modifier = modifier
    ){
        Text("VISITANT")
        if (awayTeamPlayers != null) {
            for(player in awayTeamPlayers){
                Button(
                    onClick = {
                        onPlayerSelected(player) },
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
                    Text("${player.number}")
                }
            }
        }
    }
}

@Composable
fun BottomBar(selectedAction : String?, onActionSelected : (String) -> Unit, actionColorMap: Map<String, Color>){


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ){
        for((action, color) in actionColorMap){
            Button(
                onClick = { onActionSelected(action)
                            Log.d("MatchScreen", "Bottom Bar: Nova acció seleccionada: $action")
                          },
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
    modifier : Modifier = Modifier,
    actionColorMap : Map<String,Color>
){

    var circle by remember {mutableStateOf<Circle?>(null)}
    val circleColor by remember(selectedAction){
        mutableStateOf(actionColorMap[selectedAction] ?: Color.Black)
    }
    Log.d("HockeyRink", "Entrant acció amb $selectedPlayer i $selectedAction")

    LaunchedEffect(selectedAction){
        circle = null
    }

    Box(modifier = Modifier){
        Image(
            painter = painterResource(id = R.drawable.pistapartit),
            contentDescription = "Pista d'hoquei",
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        if (selectedPlayer != null && selectedAction != null) {
                            Log.d("RinkHockey", "Just abans de crear acció: $selectedPlayer - $selectedAction")
                            val action = Action(
                                homeTeam = selectedPlayer.isHome,
                                actionType = selectedAction,
                                position = offset
                            )
                            Log.d("MatchScreen", "Nova acció creada: ${action.actionType}")
                            matchViewModel.registerAction(action, currentPart, selectedPlayer)
                            matchViewModel.saveMatchToFirebase()
                            circle = Circle(offset, selectedAction)

                        }
                    }
                }
        )
        Canvas(modifier = Modifier.fillMaxSize()){
            circle?.let {
                drawCircle(circleColor, radius = 14f, center = it.position)
                Log.d("HockeyRink", "Dibuixant cercle: ${it.position}")
            }
        }
    }
}
    fun Int.formatAsMatchTime(): String {
        val minutes = this / 60
        val seconds = this % 60
        return String.format("%02d:%02d", minutes, seconds)
    }






