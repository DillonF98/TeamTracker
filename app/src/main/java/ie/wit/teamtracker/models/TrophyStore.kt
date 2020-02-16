package ie.wit.teamtracker.models

interface TrophyStore {
    fun findAll() : List<TrophyModel>
    fun create(trophy: TrophyModel)
    fun delete(trophy: TrophyModel)

}