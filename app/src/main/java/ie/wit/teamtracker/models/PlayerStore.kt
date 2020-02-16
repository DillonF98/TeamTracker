package ie.wit.teamtracker.models

interface PlayerStore {
    fun findAll() : List<PlayerModel>
    fun findById(id: Long) : PlayerModel?
    fun create(player: PlayerModel)
    fun delete(player: PlayerModel)

}