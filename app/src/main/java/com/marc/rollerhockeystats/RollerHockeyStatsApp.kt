package com.marc.rollerhockeystats

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase

class RollerHockeyStatsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeFirebase()
    }

    private fun initializeFirebase() {
        val options = FirebaseOptions.Builder()
            .setApplicationId("com.marc.rollerhockeystats")
            .setDatabaseUrl("https://rinkhockeystats-default-rtdb.europe-west1.firebasedatabase.app")
            .build()
        FirebaseApp.initializeApp(this, options, "rinkhockeystats")
        FirebaseDatabase.getInstance("https://rinkhockeystats-default-rtdb.europe-west1.firebasedatabase.app").setPersistenceEnabled(true)
    }
}