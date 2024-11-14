package com.marc.rollerhockeystats.ui.models

data class Player(val name : String, val number : Int){

    fun isValid(): Boolean {
        return name.isNotEmpty() && name.length < 20 && number >= 0 && number <= 100
    }
}
