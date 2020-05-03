package ie.wit.teamtracker.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FanMomentModel(var uid: String? = "",
                          var title: String = "",
                          var name: String = "",
                          var date: String = "",
                          var desc: String = "",
                          var latitude: Double = 0.0,
                          var longitude: Double = 0.0,
                          var profilepic: String = "",
                          var isfavourite: Boolean = false,
                          var email: String? = "") : Parcelable

{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "title" to title,
            "name" to name,
            "date" to date,
            "desc" to desc,
            "latitude" to latitude,
            "longitude" to longitude,
            "longitude" to longitude,
            "isfavourite" to isfavourite,
            "email" to email

        )
    }
}