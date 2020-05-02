package ie.wit.teamtracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.R
import ie.wit.teamtracker.models.TrophyModel
import kotlinx.android.synthetic.main.card_trophy.view.*

interface TrophyListener {
    fun onTrophyClick(trophy: TrophyModel)
}

class TrophyAdapter constructor(private var trophys: ArrayList<TrophyModel>,
                                private val listener: TrophyListener)
    : RecyclerView.Adapter<TrophyAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_trophy,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val trophy = trophys[holder.adapterPosition]
        holder.bind(trophy, listener)
    }

    override fun getItemCount(): Int = trophys.size


    fun removeAt(position: Int) {
        trophys.removeAt(position)
        notifyItemRemoved(position)
    }

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(trophy: TrophyModel, listener: TrophyListener) {

            itemView.tag = trophy
            itemView.tName.text = trophy.trophyName
            itemView.tAmount.text = trophy.trophyAmount
            itemView.setOnClickListener{listener.onTrophyClick(trophy)}

        }
    }

}