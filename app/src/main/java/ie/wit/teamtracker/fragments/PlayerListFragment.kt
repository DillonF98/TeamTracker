package ie.wit.teamtracker.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ie.wit.R
import ie.wit.teamtracker.adapters.PlayerAdapter
import ie.wit.teamtracker.adapters.PlayerListener
import ie.wit.teamtracker.main.PlayerApp
import ie.wit.teamtracker.models.PlayerModel
import ie.wit.teamtracker.utils.createLoader
import ie.wit.teamtracker.utils.hideLoader
import ie.wit.teamtracker.utils.showLoader
import kotlinx.android.synthetic.main.fragment_player_list.*
import kotlinx.android.synthetic.main.fragment_player_list.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class PlayerListFragment : Fragment(), AnkoLogger, PlayerListener {

    lateinit var app: PlayerApp
    lateinit var loader : AlertDialog
    lateinit var root: View



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as PlayerApp

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_player_list, container, false)
        activity?.title = getString(R.string.action_team)

        root.playerRecyclerView.layoutManager = LinearLayoutManager(activity)
        return root

    }


    companion object {
        @JvmStatic
        fun newInstance() =
            PlayerListFragment().apply {
                arguments = Bundle().apply { }
            }
    }


    override fun onPlayerClick(player: PlayerModel) {
    }

    override fun onResume() {
        super.onResume()
        getAllPlayers(app.auth.currentUser!!.uid)
    }

    fun getAllPlayers(userId: String?) {
        loader = createLoader(activity!!)
        showLoader(loader, "Downloading Players from Firebase")
        val playersList = ArrayList<PlayerModel>()
        app.database.child("user-players").child(userId!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    info("Firebase Player error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    hideLoader(loader)
                    val children = snapshot.children
                    children.forEach {
                        val legend = it.
                        getValue<PlayerModel>(PlayerModel::class.java)

                        playersList.add(legend!!)
                        root.playerRecyclerView.adapter =
                            PlayerAdapter(playersList, this@PlayerListFragment)
                        root.playerRecyclerView.adapter?.notifyDataSetChanged()


                        app.database.child("user-players").child(userId)
                            .removeEventListener(this)
                    }
                }
            })
    }

}
