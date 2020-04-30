package ie.wit.teamtracker.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlayerModel(var uid: String? = "",
                       var name: String = "",
                       var age: String = "",
                       var position: String = "",
                       var yearSigned: String = "",
                       var signingValue: String ="",
                       var currentValue: String ="",
                       var nationality: String ="",
                       var profilepic: String = "",
                       var email: String? = "") : Parcelable

{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "name" to name,
            "age" to age,
            "position" to position,
            "yearSigned" to yearSigned,
            "signingValue" to signingValue,
            "currentValue" to currentValue,
            "nationality" to nationality,
            "profilepic" to profilepic,
            "email" to email

        )
    }
}