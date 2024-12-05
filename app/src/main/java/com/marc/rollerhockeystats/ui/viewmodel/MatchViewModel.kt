package com.marc.rollerhockeystats.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.marc.rollerhockeystats.ui.models.Match
import com.marc.rollerhockeystats.ui.models.Player
import com.marc.rollerhockeystats.ui.models.StaffMember
import com.marc.rollerhockeystats.ui.models.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MatchViewModel : ViewModel() {

    private val firebaseDatabase = FirebaseDatabase.getInstance() //Realtime Database

    //aquesta estructuració aporta més seguretat: els atributs es poden modificar dins del viewModel, des de fora només llegir
    private val _matchCategory = MutableStateFlow("")
    val matchCategory : StateFlow<String> = _matchCategory

    private val _ubication = MutableStateFlow("")
    val ubication : StateFlow<String> = _ubication

    private val _selectedDate = MutableStateFlow<Long?>(null)
    val selectedDate : StateFlow<Long?> = _selectedDate

    private val _halfs = MutableStateFlow("")
    val halfs : StateFlow<String> = _halfs

    private val _minutes = MutableStateFlow("")
    val minutes : StateFlow<String> = _minutes

    private val _homeTeamName = MutableStateFlow("")
    val homeTeamName : StateFlow<String> = _homeTeamName

    private val _homeTeamStaff = MutableStateFlow<List<StaffMember>>(emptyList())
    val homeTeamStaff : StateFlow<List<StaffMember>> = _homeTeamStaff

    private val _homeTeamPlayers = MutableStateFlow<List<Player>>(emptyList())
    val homeTeamPlayers : StateFlow<List<Player>> = _homeTeamPlayers

    private val _awayTeamName = MutableStateFlow("")
    val awayTeamName : StateFlow<String> = _awayTeamName

    private val _awayTeamStaff = MutableStateFlow<List<StaffMember>>(emptyList())
    val awayTeamStaff : StateFlow<List<StaffMember>> = _awayTeamStaff

    private val _awayTeamPlayers = MutableStateFlow<List<Player>>(emptyList())
    val awayTeamPlayers : StateFlow<List<Player>> =  _awayTeamPlayers

    private val _match = MutableStateFlow<Match?>(null)
    val match : StateFlow<Match?> = _match

    fun setMatchCategory(value : String){
        _matchCategory.value = value
    }

    fun setUbication(value : String){
        _ubication.value = value
    }

    fun setSelectedDate(value : Long?){
        _selectedDate.value = value
    }

    fun setHalfs(value : String){
        _halfs.value = value
    }

    fun setMinutes(value : String){
        _minutes.value = value
    }

    fun setHomeTeamName(value : String){
        _homeTeamName.value = value
    }

    fun setMatch(match : Match){
        _match.value = match
    }

    fun addStaffMember(staffMember : StaffMember, whichTeam : String){
        if(whichTeam == "home" || whichTeam == "Home") //evitem problemes per majuscules/minuscules
            _homeTeamStaff.value += staffMember
        else if(whichTeam == "away" || whichTeam == "Away")
            _awayTeamStaff.value += staffMember
    }

    fun addPlayer(player : Player, whichTeam: String){

            if(whichTeam == "home" || whichTeam == "Home") { //evitem problemes per majuscules/minuscules
                if (_homeTeamPlayers.value.size < 10)
                    _homeTeamPlayers.value += player
            }
            else if(whichTeam == "away" || whichTeam == "Away"){
                if(_awayTeamPlayers.value.size < 10)
                    _awayTeamPlayers.value += player
            }
    }

    fun removeTeam(team : Team){

    }
}