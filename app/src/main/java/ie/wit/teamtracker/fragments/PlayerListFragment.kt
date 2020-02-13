package ie.wit.teamtracker.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.R
import ie.wit.teamtracker.adapters.PlayerAdapter
import ie.wit.teamtracker.main.PlayerApp
import ie.wit.teamtracker.models.PlayerModel
import kotlinx.android.synthetic.main.fragment_player_list.*
import kotlinx.android.synthetic.main.fragment_player_list.view.*

class PlayerListFragment : Fragment() {

    lateinit var app: PlayerApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as PlayerApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_player_list, container, false)

        root.recyclerView.layoutManager = LinearLayoutManager(activity)
        root.recyclerView.adapter = PlayerAdapter(app.playerStore.findAll())

        return root
        loadPlayers()


    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PlayerListFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    private fun loadPlayers() {
        showPlayers(app.playerStore.findAll())
    }

    private fun showPlayers (players: List<PlayerModel>) {
        recyclerView.adapter = PlayerAdapter(players)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadPlayers()
        super.onActivityResult(requestCode, resultCode, data)
    }

}
