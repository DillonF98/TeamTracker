package ie.wit.teamtracker.main

import android.app.Application
import android.util.Log
import ie.wit.teamtracker.models.*

class PlayerApp : Application() {


    lateinit var playerStore: PlayerStore
    lateinit var legendStore: LegendStore


    override fun onCreate() {
        super.onCreate()

        //playerStore = PlayerMemStore()
        playerStore = PlayerJSONStore(applicationContext)
        legendStore = LegendJSONStore(applicationContext)

        Log.v("Team Tracker","Team Tracker App started")
    }
}