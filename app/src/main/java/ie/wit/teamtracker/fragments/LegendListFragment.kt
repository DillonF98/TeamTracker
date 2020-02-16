package ie.wit.teamtracker.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.R
import ie.wit.teamtracker.adapters.LegendAdapter
import ie.wit.teamtracker.adapters.PlayerAdapter
import ie.wit.teamtracker.main.PlayerApp
import ie.wit.teamtracker.models.LegendModel
import ie.wit.teamtracker.models.PlayerModel
import kotlinx.android.synthetic.main.fragment_player_list.*
import kotlinx.android.synthetic.main.fragment_player_list.view.*

class LegendListFragment : Fragment() {

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
        var root = inflater.inflate(R.layout.fragment_legend_list, container, false)
        activity?.title = getString(R.string.action_legends)


        root.recyclerView.layoutManager = LinearLayoutManager(activity)
        root.recyclerView.adapter = LegendAdapter(app.legendStore.findAll())

        return root
        loadLegends()


    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LegendListFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    private fun loadLegends() {
        showLegends(app.legendStore.findAll())
    }

    private fun showLegends (legends: List<LegendModel>) {
        recyclerView.adapter = LegendAdapter(legends)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadLegends()
        super.onActivityResult(requestCode, resultCode, data)
    }

}
