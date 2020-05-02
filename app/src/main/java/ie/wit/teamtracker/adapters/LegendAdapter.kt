
package ie.wit.teamtracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.squareup.picasso.Picasso
import ie.wit.R
import ie.wit.teamtracker.models.LegendModel
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.card_legend.view.*
import kotlinx.android.synthetic.main.card_player.view.*

interface LegendListener {
    fun onLegendClick(legend: LegendModel)
}

class LegendAdapter(options: FirebaseRecyclerOptions<LegendModel>,
                    private val listener: LegendListener?)
    : FirebaseRecyclerAdapter<LegendModel,
        LegendAdapter.LegendViewHolder>(options) {

    class LegendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(legend: LegendModel, listener: LegendListener) {
            with(legend) {

                itemView.tag = legend
                itemView.lName.text = legend.legendName
                itemView.caps.text = legend.caps
                itemView.lTrophies.text = legend.trophiesWon
                itemView.yrsAtClub.text = legend.yrsAtClub
                itemView.setOnClickListener{listener.onLegendClick(legend)}
/*
                if(listener is PlayersAllFragment)
                    ; // Do Nothing, Don't Allow 'Clickable' Rows
                else
                    itemView.setOnClickListener { listener.LegendClick(legend) }

 */

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LegendViewHolder {

        return LegendViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_legend, parent, false))
    }

    override fun onBindViewHolder(holder: LegendViewHolder, position: Int, model: LegendModel) {
        holder.bind(model,listener!!)
    }

    override fun onDataChanged() {
        // Called each time there is a new data snapshot. You may want to use this method
        // to hide a loading spinner or check for the "no documents" state and update your UI.
        // ...
    }
}


