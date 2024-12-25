package com.marc.rollerhockeystats

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.marc.rollerhockeystats.ui.viewmodel.MatchesViewModel
import com.marc.rollerhockeystats.ui.navigation.MainNavHost
import com.marc.rollerhockeystats.ui.theme.RollerHockeyStatsTheme



class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("MainActivity", "Creant MatchesViewModel")
        val matchesViewModel : MatchesViewModel by viewModels()
        Log.d("MainActivity", "Creant MatchesViewModel : $matchesViewModel")

        Log.d("MainActivity", "MatchesViewModel inicialitzat: $matchesViewModel")
        setContent {
            Log.d("MainActivity", "Abans de setContent: $matchesViewModel")
            RollerHockeyStatsTheme {
                Log.d("MainActivity", "Dins de setContent : $matchesViewModel")
                MainNavHost(matchesViewModel)
            }
            Log.d("MainActivity", "Després de setContentm: $matchesViewModel")
        }
    }


}






