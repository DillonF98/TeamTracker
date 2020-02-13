package ie.wit.teamtracker.main

import android.app.Application
import android.util.Log
import ie.wit.teamtracker.models.PlayerJSONStore
import ie.wit.teamtracker.models.PlayerMemStore
import ie.wit.teamtracker.models.PlayerStore

class PlayerApp : Application() {


    lateinit var playerStore: PlayerStore

    override fun onCreate() {
        super.onCreate()

        //playerStore = PlayerMemStore()
        playerStore = PlayerJSONStore(applicationContext)

        Log.v("Team Tracker","Team Tracker App started")
    }
}