package com.marc.rollerhockeystats.ui.models

data class StaffMember(
    val StaffId : String = "",
    val name : String = "",
    val surname1 : String = "",
    val surname2 : String = "",
    val role : String = "",
    val penalties : List<StaffPenalty> = emptyList() //revisar que pot ser null
)
