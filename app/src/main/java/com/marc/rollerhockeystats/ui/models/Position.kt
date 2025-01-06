package com.marc.rollerhockeystats.ui.models

import androidx.compose.ui.geometry.Offset

data class Position(
    val x : Float = 0f,
    val y : Float = 0f
) {
    fun toOffset(): Offset {
        return Offset(x,y)
    }
}
