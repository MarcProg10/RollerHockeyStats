package com.marc.rollerhockeystats.ui.models

data class Match(
    val id : String = "",
    val category : String = "",
    val ubication : String = "",
    val halfs : Int = 0,
    val minutes : Int = 0,
    val homeTeam : String = "",
    val awayTeam : String = "",
    val playersActions : Map<String, Action> = emptyMap(),
    val staffPenalties : Map<String, Action> = emptyMap()
)

