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

val JSON_FILE = "players.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<ArrayList<PlayerModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class PlayerJSONStore : PlayerStore, AnkoLogger {

    val context: Context
    var players = mutableListOf<PlayerModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<PlayerModel> {
        return players
    }

    override fun findById(id: Long): PlayerModel? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create(player: PlayerModel) {
        player.id = generateRandomId()
        players.add(player)
        serialize()
    }

    override fun delete(player: PlayerModel) {
        players.remove(player)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(players, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        players = Gson().fromJson(jsonString, listType)
    }
}