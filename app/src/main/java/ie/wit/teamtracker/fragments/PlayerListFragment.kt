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
import ie.wit.teamtracker.adapters.PlayerListener
import ie.wit.teamtracker.main.PlayerApp
import ie.wit.teamtracker.models.PlayerModel
import kotlinx.android.synthetic.main.card_player.view.*
import kotlinx.android.synthetic.main.fragment_player_list.*
import kotlinx.android.synthetic.main.fragment_player_list.view.*

@Suppress("UNREACHABLE_CODE")
class PlayerListFragment : Fragment(), PlayerListener {

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
        activity?.title = getString(R.string.action_team)


        root.playerRecyclerView.layoutManager = LinearLayoutManager(activity)
        root.playerRecyclerView.adapter = PlayerAdapter(app.playerStore.findAll(), this)

        //deletePlayerButton(root)

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

    override fun onPlayerClick(player: PlayerModel) {
        app.playerStore.delete(player)
        loadPlayers()
    }

    private fun loadPlayers() {
        showPlayers(app.playerStore.findAll())
    }

    private fun showPlayers (players: List<PlayerModel>) {
        playerRecyclerView.adapter = PlayerAdapter(players,this)
        playerRecyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadPlayers()
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun deletePlayerButton(layout: View, player: PlayerModel){

        layout.deleteButton.setOnClickListener{
            app.playerStore.delete(player)


        }

    }



}
