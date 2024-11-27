package com.marc.rollerhockeystats.ui.models

data class Player(
    val id : String = "",
    val name : String = "",
    val surname1 : String = "",
    val surname2 : String,
    val number : Int = 0
    val actions : List<Action> = emptyList() //revisar que pot ser null
)
{

    fun isValid(): Boolean {
        return name.isNotEmpty() && surname1.isNotEmpty() && name.length < 20 && number >= 0 && number <= 100
    }
}
