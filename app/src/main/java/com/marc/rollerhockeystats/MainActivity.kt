package com.marc.rollerhockeystats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.marc.rollerhockeystats.ui.viewmodel.MatchesViewModel
import com.marc.rollerhockeystats.ui.navigation.MainNavHost
import com.marc.rollerhockeystats.ui.theme.RollerHockeyStatsTheme



class MainActivity : ComponentActivity() {

    val matchesViewModel : MatchesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RollerHockeyStatsTheme {
                MainNavHost(matchesViewModel)
            }
        }
    }
}






