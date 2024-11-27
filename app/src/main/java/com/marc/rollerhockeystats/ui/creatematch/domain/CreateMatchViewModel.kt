package com.marc.rollerhockeystats.ui.creatematch.domain

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CreateMatchViewModel : ViewModel() {

    private val firebaseDatabase = FirebaseDatabase.getInstance() //Realtime Database

    //aquesta estructuració aporta més seguretat: els atributs es poden modificar dins del viewModel, des de fora només llegir
    private val _matchCategory = MutableStateFlow("")
    val matchCategory : StateFlow<String> = _matchCategory

    private val _ubication = MutableStateFlow("")
    val ubication : StateFlow<String>  = _ubication

    private val _selectedDate = MutableStateFlow<Long?>(null)
    val selectedDate : StateFlow<Long?> = _selectedDate

    private val _halfs = MutableStateFlow("")
    val halfs : StateFlow<String> = _halfs

    private val _minutes = MutableStateFlow("")
    val minutes : StateFlow<String> = _minutes

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

    fun saveMatch(){
        val match = Match(
            category = matchCategory.value,
            ubication = ubication.value,
            selectedDate = selectedDate.value,
            halfs = halfs.value,
            minutes = minutes.value,




        )
    }
}