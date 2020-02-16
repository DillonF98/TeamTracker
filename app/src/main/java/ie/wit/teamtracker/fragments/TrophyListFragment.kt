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
import ie.wit.teamtracker.adapters.TrophyAdapter
import ie.wit.teamtracker.adapters.TrophyListener
import ie.wit.teamtracker.main.PlayerApp
import ie.wit.teamtracker.models.PlayerModel
import ie.wit.teamtracker.models.TrophyModel
import kotlinx.android.synthetic.main.card_player.view.*
import kotlinx.android.synthetic.main.fragment_player_list.*
import kotlinx.android.synthetic.main.fragment_player_list.view.*
import kotlinx.android.synthetic.main.fragment_trophy_list.*
import kotlinx.android.synthetic.main.fragment_trophy_list.view.*

class TrophyListFragment : Fragment(), TrophyListener {

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
        var root = inflater.inflate(R.layout.fragment_trophy_list, container, false)
        activity?.title = getString(R.string.action_trophy)


        root.trophyRecyclerView.layoutManager = LinearLayoutManager(activity)
        root.trophyRecyclerView.adapter = TrophyAdapter(app.trophyStore.findAll(), this)

        return root
        loadTrophys()


    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TrophyListFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun onTrophyClick(trophy: TrophyModel) {
        app.trophyStore.delete(trophy)
        loadTrophys()
    }

    private fun loadTrophys() {
        showTrophys(app.trophyStore.findAll())
    }

    private fun showTrophys (trophys: List<TrophyModel>) {
        trophyRecyclerView.adapter = TrophyAdapter(trophys,this)
        trophyRecyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadTrophys()
        super.onActivityResult(requestCode, resultCode, data)
    }




}
