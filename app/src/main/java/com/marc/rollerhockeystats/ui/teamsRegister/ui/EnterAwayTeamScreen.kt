package com.marc.rollerhockeystats.ui.teamsRegister.ui

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.marc.rollerhockeystats.ui.models.Player
import com.marc.rollerhockeystats.ui.models.StaffMember
import com.marc.rollerhockeystats.ui.viewmodel.MatchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterAwayTeamScreen(viewModel: MatchViewModel, navController: NavController ){

    var teamName by remember { mutableStateOf("") }
    val staff by viewModel.awayTeamStaff.collectAsState() //tindrà maxim 5 components
    val teamPlayers by viewModel.awayTeamPlayers.collectAsState() //maxim 10 components
    var playerName by remember { mutableStateOf("") }
    var playerNumber by remember { mutableStateOf("") }
    val context = LocalContext.current
    var staffMemberName by remember { mutableStateOf("") }
    var staffMemberRole by remember { mutableStateOf("") }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Introducció equip visitant") },
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

                Text(text = "Introdueix dades de l'equip visitant:")
                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = teamName,
                    onValueChange = { teamName = it },
                    label = { Text("Nom de l'equip visitant") },
                    maxLines = 1
                )

                Spacer(Modifier.height(10.dp))
                Text(text = "Plantilla")
                Spacer(Modifier.height(10.dp))
                Row {
                    TextField(
                        value = playerName,
                        onValueChange = { playerName = it },
                        label = { Text("Nom") },
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    TextField(
                        value = playerNumber,
                        onValueChange = { playerNumber = it },
                        label = { Text("Dorsal") },
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        val playerNumberInt = playerNumber.toInt() //or null???
                        val player = Player.create(playerName, playerNumberInt)
                        if (player.isValid() && teamPlayers.size < 10) {
                            viewModel.addPlayer(player, "away")
                            playerName = ""
                            playerNumber = ""
                        } else if (teamPlayers.size >= 10)
                            Toast.makeText(
                                context,
                                "No es poden afegir més membres a la plantilla!",
                                Toast.LENGTH_SHORT
                            ).show()
                        else
                            println("Error al crear el membre de la plantilla")

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
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    TextField(
                        value = staffMemberRole,
                        onValueChange = { staffMemberRole = it },
                        label = { Text("Rol") },
                        maxLines = 1
                    )

                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        val staffMember = StaffMember.create(staffMemberName, staffMemberRole)
                        if (staffMember.isValid() && staff.size < 5) {
                            viewModel.addStaffMember(staffMember, "home")
                            staffMemberName = ""
                            staffMemberRole = ""
                        } else if (staff.size >= 5)
                            Toast.makeText(
                                context,
                                "No es poden afegir més de 5 membres d'staff!",
                                Toast.LENGTH_SHORT
                            ).show()
                    }
                ) {
                    Text(text = "Afegir membre staff")
                }

                Spacer(modifier = Modifier.height(30.dp))
                Button(onClick = { navController.navigate("matchScreen") }) {
                    Text(text = "Registrar equip visitant".uppercase())
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

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun EnterAwayTeamPreview(){
//    EnterAwayTeamScreen()
//}
