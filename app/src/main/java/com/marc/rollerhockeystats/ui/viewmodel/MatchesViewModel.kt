package com.marc.rollerhockeystats.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.marc.rollerhockeystats.ui.models.Match

class MatchesViewModel : ViewModel() {

    private val database = Firebase.database
    val matchesReference = database.getReference("matches")

    private val _matches = mutableStateListOf<Match>()
    val matches : List<Match> = _matches

    fun addMatch(match : Match){
        _matches.add(match)
    }

    fun updateMatch(match : Match) : Boolean {
        val index = _matches.indexOfFirst { it.id == match.id }
        if(index != -1){ //cas d'actualització correcta
            _matches[index] = match
            return true
        }
        return false //cas de no actualització
    }

    override fun onCleared(){
        super.onCleared()
        database.goOffline()
    }
}