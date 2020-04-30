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
import ie.wit.teamtracker.adapters.LegendAdapter
import ie.wit.teamtracker.adapters.LegendListener
import ie.wit.teamtracker.adapters.PlayerAdapter
import ie.wit.teamtracker.adapters.PlayerListener
import ie.wit.teamtracker.main.PlayerApp
import ie.wit.teamtracker.models.LegendModel
import ie.wit.teamtracker.models.PlayerModel
import ie.wit.teamtracker.utils.createLoader
import ie.wit.teamtracker.utils.hideLoader
import ie.wit.teamtracker.utils.showLoader
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

    override fun onResume() {
        super.onResume()
        getAllLegends(app.auth.currentUser!!.uid)
    }

    fun getAllLegends(userId: String?) {
        loader = createLoader(activity!!)
        showLoader(loader, "Downloading Legends from Firebase")
        val legendsList = ArrayList<LegendModel>()
        app.database.child("user-legends").child(userId!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    info("Firebase Legend error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    hideLoader(loader)
                    val children = snapshot.children
                    children.forEach {
                        val legend = it.getValue<LegendModel>(LegendModel::class.java)
                        legendsList.add(legend!!)
                        root.legendRecyclerView.adapter =
                            LegendAdapter(legendsList, this@LegendListFragment)
                        root.legendRecyclerView.adapter?.notifyDataSetChanged()


                        app.database.child("user-legends").child(userId)
                            .removeEventListener(this)
                    }
                }
            })
    }

    override fun onLegendClick(legend: LegendModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
