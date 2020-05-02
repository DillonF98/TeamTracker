package ie.wit.teamtracker.main

import android.app.Application
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class PlayerApp : Application() {

    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference
    lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate() {
        super.onCreate()
        Log.v("Team Tracker","Team Tracker App started")
    }
}