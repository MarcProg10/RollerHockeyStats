package com.marc.rollerhockeystats.ui.teamsRegister.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.marc.rollerhockeystats.ui.models.Player
import com.marc.rollerhockeystats.ui.viewmodel.MatchViewModel
import com.marc.rollerhockeystats.ui.models.StaffMember
import com.marc.rollerhockeystats.ui.viewmodel.MatchViewModelFactory
import com.marc.rollerhockeystats.ui.viewmodel.MatchesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterHomeTeamScreen(matchId : String, navController: NavController, matchesViewModel : MatchesViewModel) {

    Log.d("EnterHomeTeamScreen", "Creant instància MatchViewModel amb matchId: $matchId")
    val viewModel : MatchViewModel = viewModel(factory = MatchViewModelFactory(matchId))
    Log.d("EnterHomeTeamScreen", "MatchViewModel creat: $viewModel")

    var teamName by remember { mutableStateOf("") }
    val teamPlayers = remember { mutableStateListOf<Player>() }
    val staff = remember { mutableStateListOf<StaffMember>() }

    var playerName by remember { mutableStateOf("") }
    var playerNumber by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    var staffMemberName by remember { mutableStateOf("") }
    var staffMemberRole by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Introducció equip local") },
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
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround

        ) {
            Column(

                modifier = Modifier
                    .weight(1.25f)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(text = "Introdueix dades de l'equip local:")
                Spacer(modifier = Modifier.height(10.dp))

                TextField( //entrem nom de l'equip
                    value = teamName,
                    onValueChange = { teamName = it },
                    label = { Text("Nom de l'equip local") },
                    maxLines = 1,
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus()}),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )

                Spacer(Modifier.height(10.dp))
                Text(text = "Plantilla")
                Spacer(Modifier.height(10.dp))
                Row {
                    TextField( //entrem nom del jugador/a
                        value = playerName,
                        onValueChange = { playerName = it },
                        label = { Text("Nom") },
                        maxLines = 1,
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus()}),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    TextField( //entrem dorsal del jugador/a
                        value = playerNumber,
                        onValueChange = { playerNumber = it },
                        label = { Text("Dorsal (0-99)") },
                        maxLines = 1,
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus()}),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Number
                        ),
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                         if(playerName.isEmpty() || playerNumber.isEmpty()){
                             Toast.makeText(
                                 context,
                                 "Nom i dorsal obligatoris!",
                                 Toast.LENGTH_SHORT
                             ).show()
                         }
                         else {
                             val playerNumberInt = playerNumber.toInt() //or null???
                             val player = Player.create(
                                 playerName,
                                 playerNumberInt,
                                 true
                             ) //es crea el jugador entrat
                             Log.d("EnterHomeTeamScreen", "Player creat: $player")
                             if (player.isValid() && teamPlayers.size < 10) {
                                 Log.d(
                                     "EnterHomeTeamScreen",
                                     "Actualitzant llista players local amb $player"
                                 )
                                 teamPlayers += player
                                 playerName = ""
                                 playerNumber = ""
                             } else if (teamPlayers.size >= 10)
                                 Toast.makeText(
                                     context,
                                     "No es poden afegir més membres a la plantilla!",
                                     Toast.LENGTH_SHORT
                                 ).show()
                             else if (!player.isValid()) {
                                 Toast.makeText(
                                     context,
                                     "Nom: màx 20 caràcters, Dorsal: 0-99",
                                     Toast.LENGTH_SHORT
                                 ).show()
                             }
                         }

                    }) {
                    Text("Introduïr membre plantilla")
                }

                Spacer(Modifier.height(10.dp))
                Text(text = "Equip tècnic")
                Spacer(Modifier.height(10.dp))

                Row {
                    TextField(
                        value = staffMemberName,
                        onValueChange = { staffMemberName = it },
                        label = { Text("Nom") },
                        maxLines = 1,
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus()}),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    TextField(
                        value = staffMemberRole,
                        onValueChange = { staffMemberRole = it },
                        label = { Text("Rol") },
                        maxLines = 1,
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus()}),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                    )

                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {

                        if(staffMemberName.isEmpty() || staffMemberRole.isEmpty()) {
                            Toast.makeText(
                                context,
                                "S'ha d'entrar Nom i Rol!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else {
                            val staffMember = StaffMember.create(staffMemberName, staffMemberRole)
                            Log.d("EnterHomeTeamScreen", "StaffMember creat: $staffMember")
                            if (staffMember.isValid() && staff.size < 5) {
                                Log.d("EnterHomeTeamScreen", "Actualitzant staff amb $staffMember")
                                staff += staffMember
                                staffMemberName = ""
                                staffMemberRole = ""
                            } else if (staff.size >= 5)
                                Toast.makeText(
                                    context,
                                    "No es poden afegir més de 5 membres d'staff!",
                                    Toast.LENGTH_SHORT
                                ).show()
                        }
                    }
                ) {
                    Text(text = "Afegir membre staff")
                }

                Spacer(modifier = Modifier.height(30.dp))
                Button(onClick = {

                    if(teamPlayers.isEmpty()){
                        Toast.makeText(
                            context,
                            "La plantilla ha de comptar, com a mínim, amb una persona participant",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else if(teamName.isEmpty()){
                        Toast.makeText(
                            context,
                            "Tot equip ha de tenir un nom a defensar!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else {
                        viewModel.updateTeam("home", teamName, teamPlayers, staff)
                        viewModel.saveMatchToFirebase()
                        viewModel.match.value?.let { matchesViewModel.updateMatch(it) }
                        navController.navigate("enterAwayTeam/$matchId")
                    }

                })
                {
                    Text(text = "Registrar equip local".uppercase())
                }
            }
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .wrapContentSize(Alignment.Center)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(8.dp),
                tonalElevation = 2.dp,

                )
            {

                Text(
                    text = "Llistat d'integrants de la plantilla",
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    modifier = Modifier
                        .weight(1f),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(teamPlayers) { player ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .height(50.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Text(
                                text = "${player.name} - ${player.number}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentSize(Alignment.Center),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .wrapContentSize(Alignment.Center)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(8.dp),
                tonalElevation = 2.dp,

                ) {
                Text(
                    text = "Llistat membres staff",
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(staff) { staffMember ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Text("${staffMember.name} - ${staffMember.role}")
                        }
                    }
                }
            }
        }
    }
}