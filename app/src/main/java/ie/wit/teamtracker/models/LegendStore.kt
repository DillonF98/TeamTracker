package ie.wit.teamtracker.models

interface LegendStore {
    fun findAll() : List<LegendModel>
    fun findById(id: Long) : LegendModel?
    fun create(player: LegendModel)
}