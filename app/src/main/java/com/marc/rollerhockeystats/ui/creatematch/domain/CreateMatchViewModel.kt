package com.marc.rollerhockeystats.ui.creatematch.domain

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CreateMatchViewModel : ViewModel() {

    var matchCategory = mutableStateOf("")
        private set
    var ubication = mutableStateOf("")
        private set
    var selectedDate = mutableStateOf<Long?> (null)
        private set
    var halfs = mutableStateOf("")
        private set
    var minutes = mutableStateOf("")
        private set

    fun setMatchCategory(value : String){
        matchCategory.value = value
    }

    fun setUbication(value : String){
        ubication.value = value
    }

    fun setSelectedDate(value : Long?){
        selectedDate.value = value
    }

    fun setHalfs(value : String){
        halfs.value = value
    }

    fun setMinutes(value : String){
        minutes.value = value
    }
}