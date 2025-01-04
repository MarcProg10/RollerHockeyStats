package com.marc.rollerhockeystats.ui.models

data class Match(
    val id : String? = "",
    val category : String = "",
    val ubication : String = "",
    val selectedDate : Long?,
    val halfs : Int = 0,
    val minutes : Int = 0,
    var homeTeam : Team? = null,
    var awayTeam : Team? = null,
    var finished : Boolean = false,
    var homeScore : Int = 0,
    var awayScore : Int = 0,
    var homeFouls : Int = 0,
    var awayFouls : Int = 0,
    var timeLeft : Int = 0,
    var currentHalf : Int = 1
){
    constructor() : this(
        id = "",
        category = "",
        ubication = "",
        selectedDate = null,
        halfs = 0,
        minutes = 0,
        homeTeam = null,
        awayTeam = null,
        finished = false,
        homeScore = 0,
        awayScore = 0,
        homeFouls = 0,
        awayFouls = 0,
        timeLeft = 0,
        currentHalf = 0
    )

    fun updateTeamMatch(teamToUpdate: Team) : Match{

        if(teamToUpdate.ishometeam){
            homeTeam = teamToUpdate
        }
        else{
            awayTeam = teamToUpdate
        }
        return this
    }

    fun updateStats(action : Action){
        val isHome = action.homeTeam
        if(isHome){
            when(action.actionType){
                "Gol" -> homeScore++
                "Falta" -> homeFouls++
            }
        }
        else{
            when(action.actionType){
                "Gol" -> awayScore++
                "Falta" -> awayFouls++
            }
        }
    }

    fun increaseHalf(){
        currentHalf++
    }
}


