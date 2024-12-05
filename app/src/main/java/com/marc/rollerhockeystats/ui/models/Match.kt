package com.marc.rollerhockeystats.ui.models

data class Match(
    val id : String = "",
    val category : String = "",
    val ubication : String = "",
    val selectedDate : Long?,
    val halfs : String = "",
    val minutes : String = "",
    val homeTeam : Team? = null,
    val awayTeam : Team? = null,
    val finished : Boolean = false
)
