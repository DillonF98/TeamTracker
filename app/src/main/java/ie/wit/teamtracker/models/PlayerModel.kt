package ie.wit.teamtracker.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlayerModel(var id: Long = 0,
                       var name: String = "",
                       var position: String = "",
                       var yearSigned: String = "",
                       var signingvalue: String ="",
                       var currentvalue: String ="",
                       var nationality: String ="",
                       var age: String = "") : Parcelable


