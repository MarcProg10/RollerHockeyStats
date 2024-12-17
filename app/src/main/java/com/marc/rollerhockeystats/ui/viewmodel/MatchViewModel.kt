package com.marc.rollerhockeystats.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MatchViewModel(matchId : String) : ViewModel() {

    private val database = Firebase.database
    private val matchReference = database.getReference("matches").child(matchId)

    //aquesta estructuració aporta més seguretat: els atributs es poden modificar dins del viewModel, des de fora només llegir

    private val _match = MutableStateFlow<Match?>(null)
    val match : StateFlow<Match?> = _match

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

    fun getStaff(whichTeam : String) : List<StaffMember>{
        if(whichTeam == "home"){
             return _match.value?.homeTeam?.staff ?: emptyList()
        }
        else if(whichTeam == "away"){
            return _match.value?.awayTeam?.staff ?: emptyList()
        }
        else
            return emptyList()
    }

    fun getPlayers(whichTeam: String) : List<Player>{
        if(whichTeam == "home")
            return _match.value?.homeTeam?.teamPlayers ?: emptyList()
        else if(whichTeam == "away")
            return _match.value?.awayTeam?.teamPlayers ?: emptyList()
        else
            return emptyList()
    }

    fun addStaffMember(staffMember : StaffMember, whichTeam : String){
        if(whichTeam == "home"){
            _match.update { thisMatch -> thisMatch?.copy(
                homeTeam = thisMatch.homeTeam?.copy(
                    staff = thisMatch.homeTeam.staff + staffMember
                )
            )
            }
        }
        else if(whichTeam == "away"){
            _match.update { thisMatch -> thisMatch?.copy(
                awayTeam = thisMatch.awayTeam?.copy(
                    staff = thisMatch.awayTeam.staff + staffMember
                )
            )}
        }
    }

    fun addPlayer(player : Player, whichTeam: String){

            if(whichTeam == "home"){
                _match.update { thisMatch -> thisMatch?.copy(
                    homeTeam = thisMatch.homeTeam?.copy(
                        teamPlayers = thisMatch.homeTeam.teamPlayers + player
                    )
                )
                }
            }
            else if(whichTeam == "away"){
                _match.update { thisMatch -> thisMatch?.copy(
                    awayTeam = thisMatch.awayTeam?.copy(
                        teamPlayers = thisMatch.awayTeam.teamPlayers + player
                    )
                )}
            }
    }

    fun updateTeam(whichTeam : String, newTeam : Team){
        if(whichTeam == "home")
            _match.update { it?.copy(homeTeam = newTeam)}
        else if(whichTeam == "away")
            _match.update { it?.copy(awayTeam = newTeam)}
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

    fun registerAction(action : Action, currentPart : Int){
        viewModelScope.launch{
            updateMatchWithAction(action,currentPart)
        }
    }

    private fun updateMatchWithAction(action : Action, currentPart : Int){
        val player = action.player
        val selectedAction = action.actionType
        val isHomeTeam = action.homeTeam

        val actionsList = when(currentPart){
            1 -> player.firstHalfActions.toMutableList()
            2 -> player.secondHalfActions.toMutableList()
            3 -> player.thirdHalfActions.toMutableList()
            4 -> player.fourthHalfActions.toMutableList()
            else -> mutableListOf()
        }
        actionsList.add(action)

        val updatedPlayer = when(selectedAction){
            "Gol" -> {
                if(isHomeTeam)
                    _homeScore.value++
                else
                    _awayScore.value++
                player.copy(goals = player.goals + 1)

            }
            "Tir" -> player.copy(shots = player.shots + 1)
            "Tir a porteria" -> player.copy(shotsOnGoal = player.shotsOnGoal + 1)
            "Assist" -> player.copy(assists = player.assists + 1)
            "Falta" -> {

                if(isHomeTeam)
                    _homeFouls.value++
                else
                    _awayFouls.value++
                player.copy(fouls = player.fouls + 1)
            }
            "Blava" -> player.copy(blueCards = player.blueCards + 1)
            "Vermella" -> player.copy(redCards = player.redCards + 1)

            else -> player
        }

        val updatedTeam = if(isHomeTeam)
                _match.value?.homeTeam?.updatePlayer(updatedPlayer)
        else
            _match.value?.awayTeam?.updatePlayer(updatedPlayer)

        _match.update { thisMatch ->
            if(isHomeTeam)
                thisMatch?.copy(homeTeam = updatedTeam ?: thisMatch.homeTeam)
            else
                thisMatch?.copy(awayTeam = updatedTeam ?: thisMatch.awayTeam)
        }

    }


}