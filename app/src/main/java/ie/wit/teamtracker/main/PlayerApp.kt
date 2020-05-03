package ie.wit.teamtracker.main

import android.app.Application
import android.location.Location
import android.net.Uri
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class PlayerApp : Application() {

    //lateinit var auth: FirebaseAuth
    lateinit var currentUser: FirebaseUser
    lateinit var database: DatabaseReference
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var storage: StorageReference
    lateinit var userImage: Uri
    lateinit var mMap : GoogleMap
    lateinit var marker : Marker
    lateinit var currentLocation : Location
    lateinit var locationClient : FusedLocationProviderClient


    override fun onCreate() {
        super.onCreate()
        Log.v("Team Tracker","Team Tracker App started")
    }
}