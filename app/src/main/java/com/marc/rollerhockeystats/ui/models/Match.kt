package com.marc.rollerhockeystats.ui.models

data class Match(
    val id : String? = "",
    val category : String = "",
    val ubication : String = "",
    val selectedDate : Long?,
    val halfs : Int = 0,
    val minutes : Int = 0,
    val homeTeam : Team? = null,
    val awayTeam : Team? = null,
    val finished : Boolean = false,
    val homeScore : Int = 0,
    val awayScore : Int = 0,
    val homeFouls : Int = 0,
    val awayFouls : Int = 0,
    val timeLeft : Int = 0
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
        timeLeft = 0
    )
}
