package ie.wit.teamtracker.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LegendModel(var uid: String? = "",
                       var legendName: String = "",
                       var caps: String = "",
                       var trophiesWon: String = "",
                       var yrsAtClub: String = "",
                       var email: String? = "") : Parcelable


{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "legendName" to legendName,
            "caps" to caps,
            "trophiesWon" to trophiesWon,
            "yrsAtClub" to yrsAtClub,
            "email" to email
        )
    }
}