package ie.wit.teamtracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import ie.wit.R
import ie.wit.teamtracker.models.TrophyModel
import kotlinx.android.synthetic.main.card_trophy.view.*



interface TrophyListener {
    fun onTrophyClick(trophy: TrophyModel)
}

class TrophyAdapter(options: FirebaseRecyclerOptions<TrophyModel>,
                    private val listener: TrophyListener?)
    : FirebaseRecyclerAdapter<TrophyModel,
        TrophyAdapter.TrophyViewHolder>(options) {

    class TrophyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(trophy: TrophyModel, listener: TrophyListener) {
            with(trophy) {
                itemView.tag = trophy
                itemView.tName.text = trophy.trophyName
                itemView.tAmount.text = trophy.trophyAmount
                itemView.setOnClickListener{listener.onTrophyClick(trophy)}
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrophyViewHolder {

        return TrophyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_trophy, parent, false))
    }

    override fun onBindViewHolder(holder: TrophyViewHolder, position: Int, model: TrophyModel) {
        holder.bind(model,listener!!)
    }

    override fun onDataChanged() {
        // Called each time there is a new data snapshot. You may want to use this method
        // to hide a loading spinner or check for the "no documents" state and update your UI.
        // ...
    }
}


