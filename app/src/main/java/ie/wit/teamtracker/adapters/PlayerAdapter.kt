import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.squareup.picasso.Picasso
import ie.wit.R
import ie.wit.teamtracker.fragments.PlayersAllFragment
import ie.wit.teamtracker.models.PlayerModel
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.card_player.view.*

interface PlayerListener {
    fun onPlayerClick(player: PlayerModel)
}

class PlayerAdapter(options: FirebaseRecyclerOptions<PlayerModel>,
                      private val listener: PlayerListener?)
    : FirebaseRecyclerAdapter<PlayerModel,
        PlayerAdapter.PlayerViewHolder>(options) {

    class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(player: PlayerModel, listener: PlayerListener) {
            with(player) {

                itemView.tag = player
                itemView.name.text = player.name
                itemView.age.text = player.age
                itemView.position.text = player.position

                if(listener is PlayersAllFragment)
                    ; // Do Nothing, Don't Allow 'Clickable' Rows
                else
                    itemView.setOnClickListener { listener.onPlayerClick(player) }

                if(!player.profilepic.isEmpty()) {
                    Picasso.get().load(player.profilepic.toUri())
                        //.resize(180, 180)
                        .transform(CropCircleTransformation())
                        .into(itemView.image)
                }
                else
                    itemView.image.setImageResource(R.mipmap.ic_launcher)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {

        return PlayerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_player, parent, false))
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int, model: PlayerModel) {
        holder.bind(model,listener!!)
    }

    override fun onDataChanged() {
        // Called each time there is a new data snapshot. You may want to use this method
        // to hide a loading spinner or check for the "no documents" state and update your UI.
        // ...
    }
}