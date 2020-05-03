import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.squareup.picasso.Picasso
import ie.wit.R
import ie.wit.teamtracker.fragments.FanMomentsAllFragment
import ie.wit.teamtracker.models.FanMomentModel
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.card_fan_moment.view.*

interface FanMomentListener {
    fun onFanMomentClick(fanMoment: FanMomentModel)
}

class FanMomentAdapter(options: FirebaseRecyclerOptions<FanMomentModel>,
                      private val listener: FanMomentListener?)
    : FirebaseRecyclerAdapter<FanMomentModel,
        FanMomentAdapter.FanMomentViewHolder>(options) {

    class FanMomentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(fanMoment: FanMomentModel, listener: FanMomentListener) {
            with(fanMoment) {

                itemView.tag = fanMoment
                itemView.name.text = fanMoment.name
                itemView.title.text = fanMoment.title
                itemView.date.text = fanMoment.date
                itemView.desc.text = fanMoment.desc

                if(listener is FanMomentsAllFragment)
                    ; // Do Nothing, Don't Allow 'Clickable' Rows
                else
                    itemView.setOnClickListener { listener.onFanMomentClick(fanMoment) }

                if(fanMoment.isfavourite) itemView.imagefavourite.setImageResource(android.R.drawable.star_big_on)

                if(!fanMoment.profilepic.isEmpty()) {
                    Picasso.get().load(fanMoment.profilepic.toUri())
                        //.resize(180, 180)
                        .transform(CropCircleTransformation())
                        .into(itemView.image)
                }
                else
                    itemView.image.setImageResource(R.mipmap.ic_launcher)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FanMomentViewHolder {

        return FanMomentViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_fan_moment, parent, false))
    }

    override fun onBindViewHolder(holder: FanMomentViewHolder, position: Int, model: FanMomentModel) {
        holder.bind(model,listener!!)
    }

    override fun onDataChanged() {
        // Called each time there is a new data snapshot. You may want to use this method
        // to hide a loading spinner or check for the "no documents" state and update your UI.
        // ...
    }
}