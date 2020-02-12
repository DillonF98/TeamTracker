package ie.wit.teamtracker.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.R
import ie.wit.teamtracker.adapters.PlayerAdapter
import ie.wit.teamtracker.main.PlayerApp
import kotlinx.android.synthetic.main.fragment_player_list.view.*


class PlayerListFragment : Fragment() {

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
        var root = inflater.inflate(R.layout.fragment_player_list, container, false)

        root.recyclerView.layoutManager = LinearLayoutManager(activity)
        root.recyclerView.adapter = PlayerAdapter(app.playerStore.findAll())

        return root

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PlayerListFragment().apply {
                arguments = Bundle().apply { }
            }
    }

}
