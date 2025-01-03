package com.marc.rollerhockeystats.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.marc.rollerhockeystats.ui.models.Action
import com.marc.rollerhockeystats.ui.models.Match
import com.marc.rollerhockeystats.ui.models.Player
import com.marc.rollerhockeystats.ui.models.StaffMember
import com.marc.rollerhockeystats.ui.models.Team
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MatchViewModel(matchId : String) : ViewModel() {

    private val database = Firebase.database("https://rinkhockeystats-default-rtdb.europe-west1.firebasedatabase.app")
    private val matchReference = database.getReference("matches").child(matchId)

    //aquesta estructuració aporta més seguretat: els atributs es poden modificar dins del viewModel, des de fora només llegir


    private val _match = MutableStateFlow<Match?>(null)
    val match : StateFlow<Match?> = _match

    init {
        // Carrega les dades del partit de Firebase
        matchReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val matchData = snapshot.getValue(Match::class.java)
                _match.value = matchData
                Log.d("MatchViewModel", "Dades del partit carregades: $matchData")
            }

            override fun onCancelled(error: DatabaseError) {
                // Gestiona els errors
                Log.e("MatchViewModel", "Error al carregar les dades del partit: ${error.message}")
            }
        })
    }

    private val _timeRunning = MutableStateFlow(false)
    val timeRunning : StateFlow<Boolean> = _timeRunning

    private val _timeLeft = MutableStateFlow(0)
    val timeLeft : StateFlow<Int> = _timeLeft

    private val _currentHalf = MutableStateFlow(1)
    val currentHalf : StateFlow<Int> = _currentHalf

    private val _homeScore = MutableStateFlow(0)
    val homeScore : StateFlow<Int> = _homeScore

    private val _awayScore = MutableStateFlow(0)
    val awayScore : StateFlow<Int> = _awayScore

    private val _homeFouls = MutableStateFlow(0)
    val homeFouls : StateFlow<Int> = _homeFouls

    private val _awayFouls = MutableStateFlow(0)
    val awayFouls : StateFlow<Int> = _awayFouls

    fun setMatch(match : Match){
        _match.value = match
    }

    fun updateTeam(whichTeam : String, name : String, players : List<Player>, staff : List<StaffMember>  ){

        if(whichTeam == "home") {
            val team = Team(
                teamName = name,
                teamPlayers = players,
                staff = staff,
                ishometeam = true
            )
            _match.update { it?.copy(homeTeam = team) }
        }
        else if(whichTeam == "away") {
            val team = Team(
                teamName = name,
                teamPlayers = players,
                staff = staff,
                ishometeam = false
            )
            _match.update { it?.copy(awayTeam = team) }
        }
    }

    fun saveMatchToFirebase(){
        viewModelScope.launch{
            val thisMatch = _match.value
            if(thisMatch != null){
                matchReference.setValue(thisMatch)
                    .addOnSuccessListener { "Partit actualitzat correctament" }
                    .addOnFailureListener { "Error d'actualitizació del partit"}
            }
        }
    }

    fun setToNextHalf(){
        _match.value?.increaseHalf()
        _currentHalf.value++
    }

    fun timeController(){
        _timeRunning.value = ! _timeRunning.value
        if(_timeRunning.value){
            viewModelScope.launch{
                while(_timeRunning.value && _timeLeft.value > 0){
                    delay(1000)
                    _timeLeft.value--
                }
            }
        }
    }

    fun resetTimeLeft(){
        _timeLeft.value = _match.value!!.minutes
    }

    fun registerAction(action : Action, currentPart : Int, player : Player){
        viewModelScope.launch{
            updateMatchWithAction(action,currentPart, player)
        }
    }

    private fun updateMatchWithAction(action : Action, currentPart : Int, player : Player){

        Log.d("MatchViewModel", "Inici actualització partit amb acció")
        var playerToUpdate = player
        val isHome = action.homeTeam
        Log.d("MatchViewModel", "Dades subjecte acció: $playerToUpdate")
        Log.d("MatchViewModel", "Acció a desar: $action")
        val actionsList = playerToUpdate.getPlayerActions(currentPart).toMutableList()

        Log.d("MatchViewModel", "Llistat d'accions: $actionsList")
        actionsList.add(action)
        Log.d("MatchViewModel", "Llistat d'accions part {$currentPart} actualitzada: $actionsList")

        //actualitzem el llistat d'accions del jugador
        if(actionsList.isNotEmpty()){
            playerToUpdate = playerToUpdate.updatePlayerActions(actionsList, currentPart)
            Log.d("MatchViewModel", "Jugador actualitzat: $playerToUpdate")
        }
//
//        //actualitzem estadístiques del jugador
//        playerToUpdate.updatePlayerStats(action)
//        Log.d("MatchViewModel", "stats jugador actualitzades: $playerToUpdate")
//
//        //actualitzem stats de l'equip
//        if(action.actionType == "Gol" || action.actionType == "Falta")
//            _match.value?.updateStats(action)
//
//        //actualitzem l'equip corresponent al jugador
//        var teamToUpdate: Team?
//        if(isHome){
//            teamToUpdate = _match.value?.homeTeam
//            teamToUpdate?.updateTeamPlayer(playerToUpdate)
//            Log.d("MatchViewModel", "Equip actualitzat: $teamToUpdate")
//        }
//        else{
//            teamToUpdate = _match.value?.awayTeam
//            teamToUpdate?.updateTeamPlayer(playerToUpdate)
//            Log.d("MatchViewModel", "Equip actualitzat: $teamToUpdate")
//        }
//
//        //actualitzem partit
//        if (teamToUpdate != null) {
//            _match.value?.updateTeamMatch(teamToUpdate)
//        }
//        Log.d("MatchViewModel", "Partit actualitzat: ${_match.value}")
    }


}