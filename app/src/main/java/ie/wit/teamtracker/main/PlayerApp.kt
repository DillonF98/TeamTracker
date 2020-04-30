package ie.wit.teamtracker.main

import android.app.Application
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class PlayerApp : Application() {

    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference

   override fun onCreate() {
        super.onCreate()
        Log.v("Team Tracker","Team Tracker App started")
    }
}