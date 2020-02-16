package ie.wit.teamtracker.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.temporal.TemporalAmount

@Parcelize
data class TrophyModel(var trophyId: Long = 0,
                       var trophyAmount: String ="",
                       var trophyName: String = "") : Parcelable