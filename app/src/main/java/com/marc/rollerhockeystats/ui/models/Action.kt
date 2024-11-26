package com.marc.rollerhockeystats.ui.models

import androidx.compose.ui.geometry.Offset

data class Action(
    val actionId: String = "",
    val playerId: String = "",
    val actionType: ActionType? = null,
    val position: Offset? = null,
    val timestamp: Long = 0L
)



