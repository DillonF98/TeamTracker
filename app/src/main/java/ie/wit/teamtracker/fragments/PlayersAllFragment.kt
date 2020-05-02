package ie.wit.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ie.wit.R
import ie.wit.teamtracker.adapters.PlayerAdapter
import ie.wit.teamtracker.adapters.PlayerListener
import ie.wit.teamtracker.fragments.PlayerListFragment
import ie.wit.teamtracker.models.PlayerModel
import ie.wit.teamtracker.utils.createLoader
import ie.wit.teamtracker.utils.hideLoader
import ie.wit.teamtracker.utils.showLoader
import kotlinx.android.synthetic.main.fragment_player_list.view.*
import org.jetbrains.anko.info

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
        setSwipeRefresh()

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PlayersAllFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun setSwipeRefresh() {
        root.playerSwipeRefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                root.playerSwipeRefresh.isRefreshing = true
                getAllUsersPlayers()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getAllUsersPlayers()
    }

    fun getAllUsersPlayers() {
        loader = createLoader(activity!!)
        showLoader(loader, "Downloading All Users Players from Firebase")
        val playersList = ArrayList<PlayerModel>()
        app.database.child("players")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    info("Firebase Player error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    hideLoader(loader)
                    val children = snapshot.children
                    children.forEach {
                        val player = it.
                        getValue<PlayerModel>(PlayerModel::class.java)

                        playersList.add(player!!)
                        root.playerRecyclerView.adapter =
                            PlayerAdapter(playersList, this@PlayersAllFragment, true)
                        root.playerRecyclerView.adapter?.notifyDataSetChanged()
                        checkSwipeRefresh()

                        app.database.child("players").removeEventListener(this)
                    }
                }
            })
    }
}