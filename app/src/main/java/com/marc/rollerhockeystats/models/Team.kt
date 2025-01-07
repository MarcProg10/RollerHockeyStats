package com.marc.rollerhockeystats.models

data class Team(
    val teamName : String = "",
    val staff : List<StaffMember> = emptyList(),
    val teamPlayers : List<Player> = emptyList(),
    val ishometeam : Boolean = false
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

    fun updateTeamWithPlayer(playerToUpdate: Player) {
        val index = teamPlayers.indexOfFirst { it.id == playerToUpdate.id }
        if(index != -1){
            copy(
                teamPlayers = teamPlayers.toMutableList().also{
                    it[index] = playerToUpdate
                }
            )
        }
    }

    fun totalShots() : Int{
        var count = 0
        for(player in teamPlayers){
            if(player.shots != 0)
                count += player.shots
        }
        return count
    }

    fun totalShotsOnGoal() : Int{
        var count = 0
        for(player in teamPlayers){
            if(player.shotsOnGoal != 0)
                count += player.shotsOnGoal
        }
        return count
    }

    fun totalFouls() : Int{
        var count = 0
        for(player in teamPlayers){
            if(player.fouls != 0)
                count += player.fouls
        }
        return count
    }

    fun totalAssists() : Int{
        var count = 0
        for(player in teamPlayers){
            if(player.assists != 0)
                count += player.assists
        }
        return count
    }

    fun totalBlueCards() : Int{
        var count = 0
        for(player in teamPlayers){
            if(player.blueCards != 0)
                count += player.blueCards
        }
        return count
    }

    fun totalRedCards() : Int{
        var count = 0
        for(player in teamPlayers){
            if(player.redCards != 0)
                count += player.redCards
        }
        return count
    }


}
