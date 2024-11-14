package com.marc.rollerhockeystats.ui.models

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.marc.rollerhockeystats.R

@Composable
fun ShowAppLogo(){
    Image(
        painter = painterResource(id = R.drawable.logotipapp),
        contentDescription = "App logo",
        modifier = Modifier
            .size(250.dp)
            .padding(bottom = 32.dp)
    )
}