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

    private val _rinkHockeyWidth = MutableStateFlow(1f)
    val rinkHockeyWidth : StateFlow<Float> = _rinkHockeyWidth

    private val _rinkHockeyHeight = MutableStateFlow(1f)
    val rinkHockeyHeight : StateFlow<Float> = _rinkHockeyHeight

    private val _statsScreen1Width = MutableStateFlow(1f)
    val statsScreen1Width : StateFlow<Float> = _statsScreen1Width

    private val _statsScreen2Width = MutableStateFlow(1f)
    val statsScreen2Width : StateFlow<Float> = _statsScreen2Width

    private val _statsScreen1Height = MutableStateFlow(1f)
    val statsScreen1Height : StateFlow<Float> = _statsScreen1Height

    private val _statsScreen2Height = MutableStateFlow(1f)
    val statsScreen2Height : StateFlow<Float> = _statsScreen2Height

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

    fun registerAction(action : Action, player : Player){
        viewModelScope.launch{
            updateMatchWithAction(action,player)
        }
    }

    private fun updateMatchWithAction(action : Action, player : Player){

        Log.d("MatchViewModel", "Inici actualització partit amb acció")
        Log.d("MatchViewModel", "Player : $player")
        var playerToUpdate = player
        var teamToUpdate : Team? = null
        Log.d("MatchViewModel", "Player a actualitzar: $playerToUpdate")
        Log.d("MatchViewModel", "Dades subjecte acció: $playerToUpdate")
        Log.d("MatchViewModel", "Acció a desar: $action")
        //val actionsList = playerToUpdate.getPlayerActions(currentPart).toMutableList()

        var newMatch = _match.value
        playerToUpdate.addPlayerActions(action, currentHalf.value)
        playerToUpdate.increaseStat(action)

        if(action.homeTeam){
            teamToUpdate = _match.value?.homeTeam
        }
        else{
            teamToUpdate = _match.value?.awayTeam
        }
        if (teamToUpdate != null) {
            teamToUpdate.updateTeamWithPlayer(playerToUpdate)
        }
        newMatch?.updateStats(action)
        _match.value = newMatch

        if(action.homeTeam){
            when(action.actionType){
                "Gol" -> _homeScore.value++
                "Foul" -> _homeFouls.value++
            }
        }
        else{
            when(action.actionType){
                "Gol" -> _awayScore.value++
                "Foul" -> _awayFouls.value++
            }
        }

        Log.d("MatchViewModel", "Partit actualitzat: ${_match.value}")
    }

    fun setMatchAsFinished() {
        _match.value?.finished = true
    }

    fun updateRinkHockeyWidth(width: Float) {
        _match.value?.rinkWidth = width
    }

    fun updateRinkHockeyHeight(height: Float) {
        _match.value?.rinkHeight = height
    }

    fun updateStatsScreen1Width(width: Float) {
        _statsScreen1Width.value = width

    }

    fun updateStatsScreen1Height(height: Float) {
        _statsScreen1Height.value = height

    }
}