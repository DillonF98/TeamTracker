package ie.wit.teamtracker.fragments

import PlayerAdapter
import PlayerListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import ie.wit.R
import ie.wit.teamtracker.models.PlayerModel
import kotlinx.android.synthetic.main.fragment_player_list.view.*

class PlayersAllFragment : PlayerListFragment(),
    PlayerListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_player_list, container, false)
        activity?.title = getString(R.string.menu_players_all)

        root.playerRecyclerView.layoutManager = LinearLayoutManager(activity)

        var query = FirebaseDatabase.getInstance()
            .reference.child("players")

        var options = FirebaseRecyclerOptions.Builder<PlayerModel>()
            .setQuery(query, PlayerModel::class.java)
            .setLifecycleOwner(this)
            .build()

        root.playerRecyclerView.adapter = PlayerAdapter(options, this)

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PlayersAllFragment().apply {
                arguments = Bundle().apply { }
            }
    }
}