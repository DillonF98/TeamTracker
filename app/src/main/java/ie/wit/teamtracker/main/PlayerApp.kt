package ie.wit.teamtracker.main

import android.app.Application
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import ie.wit.teamtracker.models.*

class PlayerApp : Application() {

    lateinit var auth: FirebaseAuth
    lateinit var playerStore: PlayerStore
    lateinit var legendStore: LegendStore
    lateinit var trophyStore: TrophyStore


    override fun onCreate() {
        super.onCreate()

        //playerStore = PlayerMemStore()
        playerStore = PlayerJSONStore(applicationContext)
        legendStore = LegendJSONStore(applicationContext)
        trophyStore = TrophyJSONStore(applicationContext)

        Log.v("Team Tracker","Team Tracker App started")
    }
}