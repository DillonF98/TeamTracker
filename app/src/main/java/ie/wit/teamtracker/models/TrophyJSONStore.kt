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

val trophy_JSON_FILE = "trophys.json"
val trophygsonBuilder = GsonBuilder().setPrettyPrinting().create()
val trophylistType = object : TypeToken<ArrayList<TrophyModel>>() {}.type

fun generateRandomTrophyId(): Long {
    return Random().nextLong()
}

class TrophyJSONStore : TrophyStore, AnkoLogger {

    val context: Context
    var trophys = mutableListOf<TrophyModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, trophy_JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<TrophyModel> {
        return trophys
    }

    override fun delete(trophy: TrophyModel) {
        trophys.remove(trophy)
        serialize()
    }

    override fun create(trophy: TrophyModel) {
        trophy.trophyId = generateRandomTrophyId()
        trophys.add(trophy)
        serialize()
    }

    private fun serialize() {
        val jsonString = trophygsonBuilder.toJson(trophys, trophylistType)
        write(context, trophy_JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, trophy_JSON_FILE)
        trophys = Gson().fromJson(jsonString, trophylistType)
    }
}