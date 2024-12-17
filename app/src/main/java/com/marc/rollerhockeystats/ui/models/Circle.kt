package com.marc.rollerhockeystats.ui.models

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class Circle(
    val position : Offset,
    val actionType : String
){
    val color: Color
        get() = when(actionType){
            "Gol" -> Color.Magenta
            "Tir" -> Color.Gray
            "T.porta" -> Color.Green
            "Assist" -> Color.Cyan
            "Falta" -> Color.Yellow
            "Blava" -> Color.Blue
            "Vermella" -> Color.Red
            else -> Color.Black
        }
}
