package com.marc.rollerhockeystats.ui.models

data class Player(
    val id: String = "",
    val name: String = "",
    val number: Int = 0,
    val ishome: Boolean = false,
    //revisar que poden ser null
    //poden arribar a disputar-se fins a quatre parts (Prebenjamins)
    var firstHalfActions: List<Action> = mutableListOf(),
    var secondHalfActions: List<Action> = mutableListOf(),
    var thirdHalfActions: List<Action> = mutableListOf(),
    var fourthHalfActions: List<Action> = mutableListOf(),
    var shots: Int = 0,
    var shotsOnGoal: Int = 0,
    var goals: Int = 0,
    var assists: Int = 0,
    var fouls: Int = 0,
    var blueCards: Int = 0,
    var redCards: Int = 0,
    var penaltiesTaken: Int = 0,
    var penaltiesScored: Int = 0,
    var fdTaken: Int = 0,
    var fdScored: Int = 0
    )
{
    companion object {
        fun create(newName : String, newNumber : Int, isHomePlayer: Boolean) : Player{
            return Player(name = newName, number = newNumber, ishome = isHomePlayer  )
        }
    }

    fun isValid(): Boolean {
        return name.isNotEmpty() && name.length < 20 && number >= 0 && number <= 100
    }

    fun getPlayerActions(currentPart : Int) : List<Action>{
        return when(currentPart) {
            1 -> firstHalfActions
            2 -> secondHalfActions
            3 -> thirdHalfActions
            4 -> fourthHalfActions
            else -> emptyList()
        }
    }

    fun updatePlayerActions(actions: List<Action>, currentPart: Int): Player {
        return when (currentPart) {
            1 -> copy(firstHalfActions = firstHalfActions + actions)
            2 -> copy(secondHalfActions = secondHalfActions + actions)
            3 -> copy(thirdHalfActions = thirdHalfActions+ actions)
            4 -> copy(fourthHalfActions = fourthHalfActions + actions)
            else -> this
        }
    }

    fun updatePlayerStats(action : Action){
        when(action.actionType){
            "Gol" -> goals++
            "Tir" -> shots++
            "T.porta" -> shotsOnGoal++
            "Assist" -> assists++
            "Foul" -> fouls++
            "Blava" -> blueCards++
            "Vermella" -> redCards++
        }
    }
}
