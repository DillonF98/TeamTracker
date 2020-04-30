package ie.wit.teamtracker.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize
import java.time.temporal.TemporalAmount

@Parcelize
data class TrophyModel(var uid: String? = "",
                       var trophyName: String = "",
                       var trophyAmount: String ="",
                       var email: String? = "") : Parcelable

{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "trophyAmount" to trophyAmount,
            "trophyName" to trophyName,
            "email" to email
        )
    }
}