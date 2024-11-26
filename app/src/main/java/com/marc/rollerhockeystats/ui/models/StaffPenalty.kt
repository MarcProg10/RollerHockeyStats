package com.marc.rollerhockeystats.ui.models

import com.google.firebase.Timestamp

data class StaffPenalty(
    val penaltyId : String = "",
    val staffId : String = "",
    val penaltyType : StaffPenaltyType? = null,
    val timestamp: Long = 0L

)
