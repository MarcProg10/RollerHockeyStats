package com.marc.rollerhockeystats.ui.models

data class Player(
    val id : String = "",
    val name : String = "",
    val number : Int = 0,
    //revisar que poden ser null
    //poden arribar a disputar-se fins a quatre parts (Prebenjamins)
    val firstHalfActions : List<Action> = emptyList(),
    val secondHalfActions : List<Action> = emptyList(),
    val thirdHalfActions : List<Action> = emptyList(),
    val fourthHalfActions : List<Action> = emptyList(),
    val shots : Int = 0,
    val shotsOnGoal : Int = 0,
    val goals : Int = 0,
    val assists : Int = 0,
    val fouls : Int = 0,
    val blueCards : Int = 0,
    val redCards : Int = 0,
    val penaltiesTaken : Int = 0,
    val penaltiesScored : Int = 0,
    val fdTaken : Int = 0,
    val fdScored : Int = 0
    )
{
    companion object {
        fun create(newName : String, newNumber : Int) : Player{
            return Player(name = newName, number = newNumber )
        }
    }

    fun isValid(): Boolean {
        return name.isNotEmpty() && name.length < 20 && number >= 0 && number <= 100
    }
}
