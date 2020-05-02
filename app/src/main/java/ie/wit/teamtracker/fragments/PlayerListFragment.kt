package ie.wit.teamtracker.fragments

import PlayerAdapter
import PlayerListener
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ie.wit.R
import ie.wit.teamtracker.main.PlayerApp
import ie.wit.teamtracker.models.PlayerModel
import ie.wit.teamtracker.utils.*
import kotlinx.android.synthetic.main.fragment_player_list.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

open class PlayerListFragment : Fragment(), AnkoLogger, PlayerListener {

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

        var query = FirebaseDatabase.getInstance()
            .reference
            .child("user-players").child(app.currentUser.uid)

        var options = FirebaseRecyclerOptions.Builder<PlayerModel>()
            .setQuery(query, PlayerModel::class.java)
            .setLifecycleOwner(this)
            .build()

        root.playerRecyclerView.adapter = PlayerAdapter(options, this)

        val swipeDeleteHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deletePlayer((viewHolder.itemView.tag as PlayerModel ).uid)
                deleteUserPlayer(app.currentUser.uid,(viewHolder.itemView.tag as PlayerModel).uid)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(root.playerRecyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onPlayerClick(viewHolder.itemView.tag as PlayerModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(root.playerRecyclerView)

        return root

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PlayerListFragment().apply {
                arguments = Bundle().apply { }
            }
    }
    fun deleteUserPlayer(userId: String, uid: String?) {
        app.database.child("user-players").child(userId).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Player error : ${error.message}")
                    }
                })
    }

    fun deletePlayer(uid: String?) {
        app.database.child("players").child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Player error : ${error.message}")
                    }
                })
    }

    override fun onPlayerClick(player: PlayerModel) {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, EditPlayerFragment.newInstance(player))
            .addToBackStack(null)
            .commit()
    }

}
