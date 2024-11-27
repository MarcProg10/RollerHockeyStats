package com.marc.rollerhockeystats.ui.models

import com.google.firebase.Timestamp

data class StaffPenalty(
    val penaltyId : String = "",
    val staffId : String = "",
    //penaltyType fa servir l'enum StaffPenaltyType. Per a desar a Firebase, cal passar l'enum a String
    //al recuperar el penaltyType de Firebase, cal passar-ho a Enum
    val penaltyType : String = "",
    val timestamp: Long = 0L

)
