package ie.wit.teamtracker.models

import android.util.Log

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class PlayerMemStore : PlayerStore {

        val players = ArrayList<PlayerModel>()

        override fun findAll(): List<PlayerModel> {
            return players
        }

        override fun findById(id:Long) : PlayerModel? {
            val foundPlayer: PlayerModel? = players.find { it.id == id }
            return foundPlayer
        }

        override fun create(player: PlayerModel) {
            player.id = getId()
            players.add(player)
            logAll()
        }

        fun logAll() {
            Log.v("Player","** Player List **")
            players.forEach { Log.v("Player","${it}") }
        }
    }
