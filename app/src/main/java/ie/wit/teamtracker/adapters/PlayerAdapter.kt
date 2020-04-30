package ie.wit.teamtracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.R
import ie.wit.teamtracker.models.PlayerModel
import kotlinx.android.synthetic.main.card_player.view.*

interface PlayerListener {
    fun onPlayerClick(player: PlayerModel)
}

class PlayerAdapter constructor(private var players: ArrayList<PlayerModel>,
                                private val listener: PlayerListener)
    : RecyclerView.Adapter<PlayerAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_player,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val player = players[holder.adapterPosition]
        holder.bind(player, listener)
    }

    override fun getItemCount(): Int = players.size


    fun removeAt(position: Int) {
        players.removeAt(position)
        notifyItemRemoved(position)
    }

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(player: PlayerModel, listener: PlayerListener) {

            itemView.name.text = player.name
            itemView.age.text = player.age
            itemView.position.text = player.position
            itemView.setOnClickListener{listener.onPlayerClick(player)}

        }
    }

}