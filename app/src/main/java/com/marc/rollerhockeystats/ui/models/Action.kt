package com.marc.rollerhockeystats.ui.models

import androidx.compose.ui.geometry.Offset

data class Action(

    val homeTeam : Boolean = false,
    //utilitzem l'enum ActionType, el qual s'ha de convertir a String (incompatibilitats Firebase)
    //al recuperar-lo de Firebase, s'ha de passar de String a ActionType
    val actionType : String = "",
    val position: Position? = null,
    //val timestamp: Long = 0L
)



