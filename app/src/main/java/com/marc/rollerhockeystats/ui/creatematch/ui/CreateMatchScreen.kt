package com.marc.rollerhockeystats.ui.creatematch.ui


import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.database.DatabaseReference
import com.marc.rollerhockeystats.ui.models.Match
import com.marc.rollerhockeystats.ui.viewmodel.MatchesViewModel
import com.marc.rollerhockeystats.ui.models.ShowAppLogo
import com.marc.rollerhockeystats.ui.models.Team
import com.marc.rollerhockeystats.ui.viewmodel.MatchViewModel
import com.marc.rollerhockeystats.ui.viewmodel.MatchViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


//TODO: s'ha de gestionar l'emmagatzament de selectedDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMatchScreen(matchesViewModel : MatchesViewModel, navController : NavHostController){

    val matchReference = matchesViewModel.matchesReference.push()
    Log.d("CreateMatchScreen", "Match reference created: $matchReference")

    val matchId = matchReference.key
    Log.d("CreateMatchScreen", "Match ID: $matchId")

    val viewModel = createMatchViewModel(checkNotNull(matchId))
    Log.d("CreateMatchScreen", "MatchViewModel created: $viewModel")
    Log.d("CreateMatchScreen", "MatchViewModel created for matchId: $matchId")

    var matchCategory by remember { mutableStateOf("") }
    var ubication by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var halfsText by remember {mutableStateOf("")}
    var minutesText by remember {mutableStateOf("")}

    val focusManager = LocalFocusManager.current //en quin element fa focus la UI
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

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
                onValueChange = {matchCategory = it},
                label = { Text("Categoria") },
                maxLines = 1,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus()}),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = halfsText,
                onValueChange = { halfsText = it  },
                label = { Text("Parts") },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ), //assegurem que s'entra un número
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus()})
            )
            Spacer(modifier = Modifier.height(10.dp) )
            TextField(
                value = minutesText,
                onValueChange = { minutesText = it },
                label = { Text("Minuts per part")},
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                    ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus()})

            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = ubication,
                onValueChange = { ubication = it },
                label = { Text("Ubicació") },
                maxLines = 1,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus()}),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )

            Spacer(modifier = Modifier.height(10.dp))

            DatePickerFieldToModal()

            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {

                    //TODO: comprovar que cap camp sigui blank
                    coroutineScope.launch {
                        val match = Match(
                            id = matchId,
                            category = matchCategory,
                            halfs = halfsText.toInt(),
                            minutes = minutesText.toInt(),
                            ubication = ubication,
                            selectedDate = selectedDate,
                            finished = false,
                            homeTeam = Team(),
                            awayTeam = Team()
                        ) //creació del partit
                        viewModel.setMatch(match) //actualtizem partit al viewModel
                        matchesViewModel.addMatch(match)
                        saveMatch(match, matchReference) //desem partit
                        navController.navigate("enterHomeTeam/$matchId")
                    }
                })
            {
                Text("Introduïr plantilles")
            }
        }
    }
}

@Composable
fun createMatchViewModel(matchId : String) : MatchViewModel {
    return viewModel(factory = MatchViewModelFactory(matchId))
}


@Composable
fun ShowErrorToast(errorMessage : String){
    Toast.makeText(LocalContext.current, errorMessage, Toast.LENGTH_SHORT).show()
}

//funció per a desar un partit a Firebase Realtime Database
//TODO: afegir no poder avançar de pantalla si no es desa correctament?

private fun saveMatch(match : Match, matchReference : DatabaseReference){

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
