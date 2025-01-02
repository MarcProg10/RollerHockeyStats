package com.marc.rollerhockeystats.ui.models

data class Team(
    val teamName : String = "",
    val staff : List<StaffMember> = emptyList(),
    val teamPlayers : List<Player> = emptyList(),
    val isHome : Boolean = false
) {

    fun updateTeamPlayer(playerToUpdate: Player): Team {
        val index = teamPlayers.indexOfFirst { it.id == playerToUpdate.id }
        if(index != -1){
            return copy(
                teamPlayers = teamPlayers.toMutableList().also{
                    it[index] = playerToUpdate
                }
            )
        }
        return this
    }



}
