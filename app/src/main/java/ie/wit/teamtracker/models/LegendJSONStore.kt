package ie.wit.teamtracker.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ie.wit.teamtracker.helpers.exists
import ie.wit.teamtracker.helpers.read
import ie.wit.teamtracker.helpers.write
import org.jetbrains.anko.AnkoLogger
import java.util.*

val Legend_JSON_FILE = "legends.json"
val LegendgsonBuilder = GsonBuilder().setPrettyPrinting().create()
val legendlistType = object : TypeToken<ArrayList<LegendModel>>() {}.type

fun generateRandomLegendId(): Long {
    return Random().nextLong()
}

class LegendJSONStore : LegendStore, AnkoLogger {

    val context: Context
    var legends = mutableListOf<LegendModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, Legend_JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<LegendModel> {
        return legends
    }

    override fun create(legend: LegendModel) {
        legend.legendId = generateRandomLegendId()
        legends.add(legend)
        serialize()
    }

    override fun delete(legend: LegendModel) {
        legends.remove(legend)
        serialize()
    }

    private fun serialize() {
        val jsonString = LegendgsonBuilder.toJson(legends, legendlistType)
        write(context, Legend_JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, Legend_JSON_FILE)
        legends = Gson().fromJson(jsonString, legendlistType)
    }
}