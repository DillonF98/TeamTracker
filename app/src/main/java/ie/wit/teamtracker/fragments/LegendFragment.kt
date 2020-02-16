package ie.wit.teamtracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ie.wit.R
import ie.wit.teamtracker.main.PlayerApp
import ie.wit.teamtracker.models.LegendModel
import kotlinx.android.synthetic.main.fragment_legend.*
import kotlinx.android.synthetic.main.fragment_legend.view.*


class LegendFragment : Fragment() {

    lateinit var app: PlayerApp
    var legend = LegendModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as PlayerApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_legend, container, false)
        activity?.title = getString(R.string.action_addLegend)
        setButtonListener(root)

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LegendFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    private fun setButtonListener(layout: View) {

        layout.addLegendButton.setOnClickListener {

            legend.legendName = lName.text.toString()
            legend.caps = caps.text.toString()
            legend.trophiesWon = trophiesWon.text.toString()
            legend.yrsAtClub = yrsAtClub.text.toString()

            if (legend.legendName.isNotEmpty() && legend.caps.isNotEmpty() && legend.trophiesWon.isNotEmpty() && legend.yrsAtClub.isNotEmpty()) {

            app.legendStore.create(legend.copy())

            }
            else {
                Toast.makeText(app, R.string.error_text, Toast.LENGTH_LONG).show()
            }

            legend.legendName = lName.setText("").toString()
            legend.caps = caps.setText("").toString()
            legend.trophiesWon = trophiesWon.setText("").toString()
            legend.yrsAtClub = yrsAtClub.setText("").toString()

        }
    }
}
