package com.marc.rollerhockeystats.ui.match.ui

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.marc.rollerhockeystats.R
import com.marc.rollerhockeystats.viewmodel.MatchViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marc.rollerhockeystats.models.Action
import com.marc.rollerhockeystats.models.Player
import com.marc.rollerhockeystats.models.ActionTypes
import com.marc.rollerhockeystats.models.Circle
import com.marc.rollerhockeystats.models.Position
import com.marc.rollerhockeystats.viewmodel.MatchViewModelFactory
import com.marc.rollerhockeystats.viewmodel.MatchesViewModel


@Composable
fun MatchScreen(
    matchId: String,
    navController: NavController,
    matchesViewModel: MatchesViewModel,
    homeScore: Int,
    awayScore: Int
){

    Log.d("MatchScreen", "Creant instància MatchViewModel amb matchId: $matchId")
    val matchViewModel : MatchViewModel = viewModel (factory = MatchViewModelFactory(matchId))
    Log.d("MatchScreen", "MatchViewModel creat: $matchViewModel")

    matchViewModel.updateHomeScore(homeScore)
    matchViewModel.updateAwayScore(awayScore)

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
                actionColorMap,
                matchesViewModel
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
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchTopBar(viewModel : MatchViewModel, navController: NavController, matchId : String){

    val currentHalf by viewModel.currentHalf.collectAsState()
    val homeScore by viewModel.homeScore.collectAsState()
    val awayScore by viewModel.awayScore.collectAsState()
    val match by viewModel.match.collectAsState()
    val halfs = match?.halfs
    var showDialog by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()

            ) {

                Row(verticalAlignment = Alignment.CenterVertically){

                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            imageVector = Icons.Rounded.Home,
                            contentDescription = "Sortir"
                        )
                    }

                    if(showDialog){
                        BasicAlertDialog(
                            onDismissRequest = { showDialog = false }
                        ){
                            Surface(
                                modifier = Modifier.wrapContentWidth().wrapContentHeight(),
                                shape = MaterialTheme.shapes.large,
                                tonalElevation = AlertDialogDefaults.TonalElevation
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text =
                                        "En cas de confirmar, se't redirigirà al menú principal i l'estat del partit es desarà",
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    TextButton(
                                        onClick = { navController.navigate("home") },
                                        modifier = Modifier.align(Alignment.End)
                                    ) {
                                        Text("Confirmar")
                                    }
                                }
                            }
                        }
                    }
                    Text("PART ${currentHalf}/${halfs}")

                }

                Text("${match?.homeTeam?.teamName} $homeScore - $awayScore ${match?.awayTeam?.teamName}")


                if(halfs != null && currentHalf < halfs){
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
                        viewModel.setMatchAsFinished()
                        viewModel.saveMatchToFirebase()
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
    selectedAction: String?,
    currentPart: Int,
    modifier: Modifier = Modifier,
    actionColorMap: Map<String, Color>,
    matchesViewModel : MatchesViewModel
) {

    var circle by remember {mutableStateOf<Circle?>(null)}
    val circleColor by remember(selectedAction){
        mutableStateOf(actionColorMap[selectedAction] ?: Color.Black)
    }
    Log.d("HockeyRink", "Entrant acció amb $selectedPlayer i $selectedAction")

    var currentAction by remember {mutableStateOf<String?>(null)}
    var currentPlayer by remember {mutableStateOf<Player?>(null)}

    LaunchedEffect(selectedAction, selectedPlayer){
        circle = null
        currentAction = selectedAction
        currentPlayer = selectedPlayer
    }

    Box(modifier = Modifier){
        Image(
            painter = painterResource(id = R.drawable.pistapartit),
            contentDescription = "Pista d'hoquei",
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        if (currentPlayer != null && currentAction != null) {
                            Log.d(
                                "RinkHockey",
                                "Just abans de crear acció: $currentPlayer - $currentAction"
                            )
                            val action = Action(
                                homeTeam = currentPlayer!!.ishome,
                                actionType = currentAction!!,
                                position = Position(offset.x, offset.y)
                            )
                            Log.d("MatchScreen", "Nova acció creada: ${action.actionType}")
                            matchViewModel.registerAction(action, currentPlayer!!)
                            matchViewModel.saveMatchToFirebase()
                            matchesViewModel.updateMatch(matchViewModel.match.value!!)
                            circle = Circle(offset, currentAction!!)
                        }
                    }
                }
        )
        Canvas(modifier = Modifier.fillMaxSize()){
            matchViewModel.updateRinkHockeyWidth(size.width)
            matchViewModel.updateRinkHockeyHeight(size.height)
            matchViewModel.saveMatchToFirebase()
            Log.d("MatchScreen", "{Width:${matchViewModel.match.value?.rinkWidth} Height:${matchViewModel.match.value?.rinkHeight}")

            circle?.let {
                drawCircle(circleColor, radius = 14f, center = it.position)
                Log.d("HockeyRink", "Dibuixant cercle: ${it.position}")
            }
        }
    }
}







