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
import ie.wit.teamtracker.adapters.LegendListener
import ie.wit.teamtracker.main.PlayerApp
import ie.wit.teamtracker.models.LegendModel
import kotlinx.android.synthetic.main.fragment_legend_list.*
import kotlinx.android.synthetic.main.fragment_legend_list.view.*
import kotlinx.android.synthetic.main.fragment_player_list.*
import kotlinx.android.synthetic.main.fragment_player_list.view.*

@Suppress("UNREACHABLE_CODE")
class LegendListFragment : Fragment(), LegendListener {

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


        root.legendRecyclerView.layoutManager = LinearLayoutManager(activity)
        root.legendRecyclerView.adapter = LegendAdapter(app.legendStore.findAll(), this)

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
        legendRecyclerView.adapter = LegendAdapter(legends,this )
        legendRecyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadLegends()
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onLegendClick(legend: LegendModel) {
        app.legendStore.delete(legend)
        loadLegends()
    }

}
