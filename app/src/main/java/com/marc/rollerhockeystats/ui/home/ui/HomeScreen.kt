package com.marc.rollerhockeystats.ui.home.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marc.rollerhockeystats.R
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun HomeScreen(){

//    Scaffold (
//        topBar= {
//            TopAppBar(title = { Text(text = "Pantalla d'inici"))}
//    )
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

            Button(onClick = { /*CREAR PARTIT*/ }, modifier = Modifier.width(200.dp)){
                Text("Crear partit")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = { /*CARREGAR PARTIT*/ }, modifier = Modifier.width(200.dp)) {
                Text("Carregar partit")

            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = { /*VISUALITZAR PARTIT*/ },modifier = Modifier.width(200.dp)) {
                Text("Visualitzar partit")
            }
        }

}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun HomeScreenPreview(){
    HomeScreen()
}