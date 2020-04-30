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
import ie.wit.teamtracker.adapters.TrophyAdapter
import ie.wit.teamtracker.adapters.TrophyListener
import ie.wit.teamtracker.main.PlayerApp
import ie.wit.teamtracker.models.PlayerModel
import ie.wit.teamtracker.models.TrophyModel
import ie.wit.teamtracker.utils.createLoader
import ie.wit.teamtracker.utils.hideLoader
import ie.wit.teamtracker.utils.showLoader
import kotlinx.android.synthetic.main.card_player.view.*
import kotlinx.android.synthetic.main.fragment_player_list.*
import kotlinx.android.synthetic.main.fragment_player_list.view.*
import kotlinx.android.synthetic.main.fragment_trophy_list.*
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

        return root


    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TrophyListFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun onTrophyClick(trophy: TrophyModel) {
    }

    override fun onResume() {
        super.onResume()
        getAllPlayers(app.auth.currentUser!!.uid)
    }

    fun getAllPlayers(userId: String?) {
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
                        val legend = it.
                        getValue<TrophyModel>(TrophyModel::class.java)

                        trophysList.add(legend!!)
                        root.trophyRecyclerView.adapter =
                            TrophyAdapter(trophysList, this@TrophyListFragment)
                        root.trophyRecyclerView.adapter?.notifyDataSetChanged()


                        app.database.child("user-trophys").child(userId)
                            .removeEventListener(this)
                    }
                }
            })
    }


}
