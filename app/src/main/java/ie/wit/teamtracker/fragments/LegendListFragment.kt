package ie.wit.teamtracker.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ie.wit.R
import ie.wit.teamtracker.adapters.LegendAdapter
import ie.wit.teamtracker.adapters.LegendListener
import ie.wit.teamtracker.main.PlayerApp
import ie.wit.teamtracker.models.LegendModel
import ie.wit.teamtracker.utils.*
import kotlinx.android.synthetic.main.fragment_legend_list.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class LegendListFragment : Fragment(), AnkoLogger, LegendListener {

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
        root = inflater.inflate(R.layout.fragment_legend_list, container, false)
        activity?.title = getString(R.string.action_legends)

        var query = FirebaseDatabase.getInstance()
            .reference
            .child("user-legends").child(app.currentUser.uid)

        var options = FirebaseRecyclerOptions.Builder<LegendModel>()
            .setQuery(query, LegendModel::class.java)
            .setLifecycleOwner(this)
            .build()

        root.legendRecyclerView.adapter = LegendAdapter(options, this)

        val swipeDeleteHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteLegend((viewHolder.itemView.tag as LegendModel ).uid)
                deleteUserLegend(app.currentUser.uid,(viewHolder.itemView.tag as LegendModel).uid)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(root.legendRecyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onLegendClick(viewHolder.itemView.tag as LegendModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(root.legendRecyclerView)
        root.legendRecyclerView.layoutManager = LinearLayoutManager(activity)
        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LegendListFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    fun deleteUserLegend(userId: String, uid: String?) {
        app.database.child("user-legends").child(userId).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Legend error : ${error.message}")
                    }
                })
    }

    fun deleteLegend(uid: String?) {
        app.database.child("legends").child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Legend error : ${error.message}")
                    }
                })
    }



    override fun onLegendClick(legend: LegendModel) {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, EditLegendFragment.newInstance(legend))
            .addToBackStack(null)
            .commit()
    }


}
