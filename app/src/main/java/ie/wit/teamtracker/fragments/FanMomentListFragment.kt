package ie.wit.teamtracker.fragments

import FanMomentAdapter
import FanMomentListener
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
import ie.wit.teamtracker.models.FanMomentModel
import ie.wit.teamtracker.utils.*
import kotlinx.android.synthetic.main.fragment_fan_moment_list.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

open class FanMomentListFragment : Fragment(), AnkoLogger, FanMomentListener {

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
        root = inflater.inflate(R.layout.fragment_fan_moment_list, container, false)
        activity?.title = getString(R.string.action_fan_moment)

        root.fanMomentRecyclerView.layoutManager = LinearLayoutManager(activity)

        var query = FirebaseDatabase.getInstance()
            .reference
            .child("user-fan-moments").child(app.currentUser.uid)

        var options = FirebaseRecyclerOptions.Builder<FanMomentModel>()
            .setQuery(query, FanMomentModel::class.java)
            .setLifecycleOwner(this)
            .build()

        root.fanMomentRecyclerView.adapter = FanMomentAdapter(options, this)

        val swipeDeleteHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteFanMoment((viewHolder.itemView.tag as FanMomentModel ).uid)
                deleteUserFanMoment(app.currentUser.uid,(viewHolder.itemView.tag as FanMomentModel).uid)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(root.fanMomentRecyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onFanMomentClick(viewHolder.itemView.tag as FanMomentModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(root.fanMomentRecyclerView)

        return root

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FanMomentListFragment().apply {
                arguments = Bundle().apply { }
            }
    }
    fun deleteUserFanMoment(userId: String, uid: String?) {
        app.database.child("user-fan-moments").child(userId).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Fan Moment error : ${error.message}")
                    }
                })
    }

    fun deleteFanMoment(uid: String?) {
        app.database.child("fan-moments").child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Fan Moment error : ${error.message}")
                    }
                })
    }

    override fun onFanMomentClick(fanMoment: FanMomentModel) {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, EditFanMomentFragment.newInstance(fanMoment))
            .addToBackStack(null)
            .commit()
    }

}
