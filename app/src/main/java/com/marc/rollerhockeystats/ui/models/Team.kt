package com.marc.rollerhockeystats.ui.models

data class Team(
    val teamName : String = "",
    val staff : List<StaffMember> = emptyList(),
    val teamPlayers : List<Player> = emptyList()
)
