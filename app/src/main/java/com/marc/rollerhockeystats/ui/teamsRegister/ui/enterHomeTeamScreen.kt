package com.marc.rollerhockeystats.ui.teamsRegister.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EnterHomeTeamScreen(){

    var teamName by remember { mutableStateOf("") }
    var staff by remember { mutableStateOf(listOf("")) } //tindrà maxim 5 components
    var teamPlayers by remember { mutableStateOf(listOf("")) } //maxim 10 components

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        TextField(
            value = teamName,
            onValueChange = {teamName = it},
            label = {Text("Nom de l'equip local")}
        )

        Spacer(Modifier.height(8.dp))
        Text(text = "Equip tècnic")
        Spacer(Modifier.height(8.dp))
        LazyColumn {
            items(staff.size) {
                index -> TextField(
                    value = staff[index],
                    onValueChange = {
                        newText -> staff = staff.toMutableList().also{
                            it[index] = newText //actualitzem textField actual
                         }
                        if( newText.isNotBlank() && index==staff.size-1 && staff.size <5)
                            staff = staff + ""
                    }, label = {Text("Introdueix membre cos tècnic")}
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(text = "Plantilla")
        Spacer(Modifier.height(8.dp))
        LazyColumn {
            items(staff.size) {
                    index -> TextField(
                value = teamPlayers[index],
                onValueChange = {
                        newText -> teamPlayers = teamPlayers.toMutableList().also{
                    it[index] = newText //actualitzem textField actual
                }
                    if( newText.isNotBlank() && index==teamPlayers.size-1 && teamPlayers.size <5)
                        teamPlayers = teamPlayers + ""
                }, label = {Text("Introdueix membre plantilla")}
                )

            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /*avançar a introduir equip visitant*/ }) {
            Text(text = "Registrar equip local")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EnterLocalPreview(){
    EnterHomeTeamScreen()
}
