package com.marc.rollerhockeystats.models

data class StaffMember(
    val staffId : String = "",
    val name : String = "",
    val role : String = "",
    val penalties : List<StaffPenalty> = emptyList(), //revisar que pot ser null
    val blueCards : Int = 0,
    val redCars : Int = 0
){
    companion object {
        fun create(newName : String, newRole : String) : StaffMember {
            return StaffMember(name = newName, role = newRole )
        }
    }

    fun isValid(): Boolean{
        if(name.isNotEmpty() && name.length < 20 && role.isNotEmpty() && role.length < 20)
            return true
        return false
    }
}



