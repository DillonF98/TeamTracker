package ie.wit.teamtracker.models

interface LegendStore {
    fun findAll() : List<LegendModel>
    fun create(legend: LegendModel)
    fun delete(legend: LegendModel)

}