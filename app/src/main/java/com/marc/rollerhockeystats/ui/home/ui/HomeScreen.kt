package com.marc.rollerhockeystats.ui.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.marc.rollerhockeystats.R

@Composable
fun HomeScreen(navController: NavHostController) {

        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Image(
                painter = painterResource(id = R.drawable.logotipapp),
                contentDescription = "App logo",
                modifier = Modifier
                    .size(250.dp)
                    .padding(bottom = 32.dp)

            )

            Text(text = "Benvingut/da a RollerHockey Stats!",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Button(onClick = { navController.navigate("createMatch") }, modifier = Modifier.width(200.dp)){
                Text("Crear partit")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = { navController.navigate("resumeMatch") }, modifier = Modifier.width(200.dp)) {
                Text("Reemprendre partit")

            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = { navController.navigate("loadFinishedMatch") },modifier = Modifier.width(200.dp)) {
                Text("Visualitzar partit")
            }
        }

}

//@Preview(
//    showSystemUi = true,
//    showBackground = true
//)
//@Composable
//fun HomeScreenPreview(navController: NavHostController){
//    HomeScreen(navController)
//}