package com.marc.rollerhockeystats.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.marc.rollerhockeystats.models.Match
import kotlinx.coroutines.launch


class MatchesViewModel : ViewModel() {

    private val database = Firebase.database("https://rinkhockeystats-default-rtdb.europe-west1.firebasedatabase.app")
    val matchesReference = database.getReference("matches")

    private val _matches = mutableStateListOf<Match>()
    val matches : List<Match> = _matches

    init {
        // Carrega les dades del partit de Firebase
        matchesReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val matchesList = snapshot.children.mapNotNull { childSnapshot ->
                    childSnapshot.getValue(Match::class.java)
                }
                _matches.clear() // Clear the existing list
                _matches.addAll(matchesList) // Add the new matches
                Log.d("MatchesViewModel", "Dades del partit carregades: $matchesList")
            }

            override fun onCancelled(error: DatabaseError) {
                // Gestiona els errors
                Log.e(
                    "MatchesViewModel",
                    "Error al carregar les dades del partit: ${error.message}"
                )
            }
        })
    }

    fun addMatch(match : Match){
        Log.d("MatchesViewModel", "Escrivint al node 'matches'")
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