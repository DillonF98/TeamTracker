package ie.wit.teamtracker.fragments

import FanMomentAdapter
import FanMomentListener
import PlayerAdapter
import PlayerListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import ie.wit.R
import ie.wit.teamtracker.models.FanMomentModel
import ie.wit.teamtracker.models.PlayerModel
import kotlinx.android.synthetic.main.fragment_fan_moment_list.view.*
import kotlinx.android.synthetic.main.fragment_player_list.view.*

class FanMomentsAllFragment : FanMomentListFragment(),
    FanMomentListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_fan_moment_list, container, false)
        activity?.title = getString(R.string.menu_fan_moments_all)

        root.fanMomentRecyclerView.layoutManager = LinearLayoutManager(activity)

        var query = FirebaseDatabase.getInstance()
            .reference.child("fan-moments")

        var options = FirebaseRecyclerOptions.Builder<FanMomentModel>()
            .setQuery(query, FanMomentModel::class.java)
            .setLifecycleOwner(this)
            .build()

        root.fanMomentRecyclerView.adapter = FanMomentAdapter(options, this)

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FanMomentsAllFragment().apply {
                arguments = Bundle().apply { }
            }
    }
}