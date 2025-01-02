package com.marc.rollerhockeystats.ui.models

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class Circle(
    val position : Offset,
    val actionType : String
){
    val color: Color
        get() = when(actionType){
            ActionTypes.GOAL -> Color.Magenta
            ActionTypes.SHOT -> Color.Gray
            ActionTypes.SHOT_ON_GOAL -> Color.Green
            ActionTypes.ASSIST -> Color.Cyan
            ActionTypes.FOUL -> Color.Yellow
            ActionTypes.BLUE_CARD -> Color.Blue
            ActionTypes.RED_CARD -> Color.Red
            else -> Color.Black
        }
}
