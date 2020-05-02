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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ie.wit.R
import ie.wit.teamtracker.adapters.TrophyAdapter
import ie.wit.teamtracker.adapters.TrophyListener
import ie.wit.teamtracker.main.PlayerApp
import ie.wit.teamtracker.models.TrophyModel
import ie.wit.teamtracker.utils.*
import kotlinx.android.synthetic.main.fragment_trophy_list.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class TrophyListFragment : Fragment(), AnkoLogger, TrophyListener  {

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
        root = inflater.inflate(R.layout.fragment_trophy_list, container, false)
        activity?.title = getString(R.string.action_trophy)

        root.trophyRecyclerView.layoutManager = LinearLayoutManager(activity)
        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = root.trophyRecyclerView.adapter as TrophyAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                deleteTrophy((viewHolder.itemView.tag as TrophyModel ).uid)
                deleteUserTrophy(app.currentUser.uid,(viewHolder.itemView.tag as TrophyModel).uid)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(root.trophyRecyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onTrophyClick(viewHolder.itemView.tag as TrophyModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(root.trophyRecyclerView)

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TrophyListFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun onResume() {
        super.onResume()
        getAllTrophys(app.currentUser.uid)
    }

    open fun setSwipeRefresh() {
        root.trophyswiperefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                root.trophyswiperefresh.isRefreshing = true
                getAllTrophys(app.currentUser.uid)
            }
        })
    }

    fun checkSwipeRefresh() {
        if (root.trophyswiperefresh.isRefreshing) root.trophyswiperefresh.isRefreshing = false
    }

    fun deleteUserTrophy(userId: String, uid: String?) {
        app.database.child("user-trophys").child(userId).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Trophy error : ${error.message}")
                    }
                })
    }

    fun deleteTrophy(uid: String?) {
        app.database.child("trophys").child(uid!!)
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

    override fun onTrophyClick(trophy: TrophyModel) {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, EditTrophyFragment.newInstance(trophy))
            .addToBackStack(null)
            .commit()
    }


    fun getAllTrophys(userId: String?) {
        loader = createLoader(activity!!)
        showLoader(loader, "Downloading Trophys from Firebase")
        val trophysList = ArrayList<TrophyModel>()
        app.database.child("user-trophys").child(userId!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    info("Firebase Trophy error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    hideLoader(loader)
                    val children = snapshot.children
                    children.forEach {
                        val trophy = it.
                        getValue<TrophyModel>(TrophyModel::class.java)

                        trophysList.add(trophy!!)
                        root.trophyRecyclerView.adapter =
                            TrophyAdapter(trophysList, this@TrophyListFragment)
                        root.trophyRecyclerView.adapter?.notifyDataSetChanged()
                        checkSwipeRefresh()

                        app.database.child("user-trophys").child(userId)
                            .removeEventListener(this)
                    }
                }
            })
    }




}
