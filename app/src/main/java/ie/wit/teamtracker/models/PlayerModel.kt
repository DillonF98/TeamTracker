package ie.wit.teamtracker.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlayerModel(var id: Long = 0,
                       var name: String = "",
                       var age: String = "",
                       var position: String = "",
                       var yearSigned: String = "",
                       var signingValue: String ="",
                       var currentValue: String ="",
                       var nationality: String ="") : Parcelable