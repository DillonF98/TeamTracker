package ie.wit.teamtracker.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LegendModel(var legendId: Long = 0,
                       var legendName: String = "",
                       var caps: String = "",
                       var trophiesWon: String = "",
                       var yrsAtClub: String = "") : Parcelable