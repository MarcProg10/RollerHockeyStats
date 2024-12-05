package com.marc.rollerhockeystats.ui.creatematch.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.database.FirebaseDatabase
import com.marc.rollerhockeystats.ui.models.Match
import com.marc.rollerhockeystats.ui.models.ShowAppLogo
import com.marc.rollerhockeystats.ui.viewmodel.MatchViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//fun CreateMatchScreen(viewModel: MatchViewModel, navController : NavHostController, onMatchCreated: () -> Unit)
//TODO: s'ha de gestionar l'emmagatzament de selectedDate
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMatchScreen(viewModel: MatchViewModel, navController : NavHostController){


    val matchCategory by viewModel.matchCategory.collectAsState()
    val halfs by viewModel.halfs.collectAsState()
    val minutes by viewModel.minutes.collectAsState()
    val ubication by viewModel.ubication.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Crear partit")},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack()}) {
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

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            ShowAppLogo()
            Text(text = "Introdueix les dades del partit:")
            Spacer(modifier = Modifier.height(10.dp))
            
            TextField(
                value = matchCategory,
                onValueChange = viewModel::setMatchCategory, //referència a la funció
                label = { Text("Categoria") },
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = halfs,
                onValueChange = viewModel::setHalfs,
                label = { Text("Parts") },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) //assegurem que s'entra un número
            )
            Spacer(modifier = Modifier.height(10.dp) )
            TextField(
                value = minutes,
                onValueChange = viewModel::setMinutes,
                label = { Text("Minuts per part")},
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = ubication,
                onValueChange = viewModel::setUbication,
                label = { Text("Ubicació") },
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(10.dp))

            DatePickerFieldToModal(
                //selectedDate = selectedDate,
                //onDateSelected = viewModel::setSelectedDate
                //TODO: com no mostrar el teclat un cop introduïda la data
            )

            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    coroutineScope.launch {
                        val match = Match(
                            category = matchCategory,
                            halfs = halfs,
                            minutes = minutes,
                            ubication = ubication,
                            selectedDate = selectedDate
                        ) //creació del partit
                        viewModel.setMatch(match) //actualtizem partit al viewModel
                        saveMatch(match) //desem partit
                        navController.navigate("enterHomeTeam")
                    }
                 }
            )
            {
                Text("Introduïr plantilles")
            }
        }
    }
}

//funció per a desar un partit a Firebase Realtime Database
//TODO: afegir no poder avançar de pantalla si no es desa correctament
private fun saveMatch(match : Match){
    val database = FirebaseDatabase.getInstance()
    val matchReference = database.getReference("matches").push()

    matchReference.setValue(match)
        .addOnSuccessListener {
            println("Partit desat satisfactoriament")
        }
        .addOnFailureListener{
            println("Error al desar el partit")
        }
}


@Composable
fun DatePickerFieldToModal(modifier: Modifier = Modifier) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate?.let { convertMillisToDate(it) } ?: "",
        onValueChange = { },
        label = { Text("Seleccionar data") },
        placeholder = { Text("MM/DD/YYYY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Selecciona data")
        },
        modifier = modifier

            .pointerInput(selectedDate) {
                awaitEachGesture {

                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = { selectedDate = it },
            onDismiss = { showModal = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}


//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun CreateMatchPreview(){
//    CreateMatchScreen()
//}
