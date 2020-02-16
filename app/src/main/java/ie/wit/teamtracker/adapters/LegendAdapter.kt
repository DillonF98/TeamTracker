package ie.wit.teamtracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.R
import ie.wit.teamtracker.models.LegendModel
import ie.wit.teamtracker.models.PlayerModel
import kotlinx.android.synthetic.main.card_legend.view.*


interface LegendListener {
    fun onLegendClick(legend: LegendModel)
}
class LegendAdapter constructor(private var legends: List<LegendModel>, private val listener: LegendListener)
    : RecyclerView.Adapter<LegendAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_legend,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val legend = legends[holder.adapterPosition]
        holder.bind(legend, listener)
    }

    override fun getItemCount(): Int = legends.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(legend: LegendModel, listener: LegendListener) {

            itemView.lName.text = legend.legendName
            itemView.caps.text = legend.caps
            itemView.lTrophies.text = legend.trophiesWon
            itemView.yrsAtClub.text = legend.yrsAtClub
            itemView.setOnClickListener{listener.onLegendClick(legend)}

        }
    }
}