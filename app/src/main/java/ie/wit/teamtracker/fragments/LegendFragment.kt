package ie.wit.teamtracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import ie.wit.R
import ie.wit.teamtracker.main.PlayerApp
import ie.wit.teamtracker.models.LegendModel
import ie.wit.teamtracker.models.PlayerModel
import ie.wit.teamtracker.utils.createLoader
import ie.wit.teamtracker.utils.hideLoader
import ie.wit.teamtracker.utils.showLoader
import kotlinx.android.synthetic.main.fragment_legend.*
import kotlinx.android.synthetic.main.fragment_legend.view.*
import kotlinx.android.synthetic.main.fragment_player.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class LegendFragment : Fragment(), AnkoLogger {

    lateinit var app: PlayerApp
    lateinit var loader: AlertDialog

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
        loader = createLoader(activity!!)
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
    fun writeNewLegend(legend: LegendModel) {
        // Create new legend at /legends & /legends/$uid
        showLoader(loader, "Adding Legend to Firebase")
        info("Firebase DB Reference : $app.database")
        val uid = app.currentUser.uid
        val key = app.database.child("legends").push().key
        if (key == null) {
            info("Firebase Error : Key Empty")
            return
        }
        legend.uid = key
        val legendValues = legend.toMap()
        val childUpdates = HashMap<String, Any>()
        childUpdates["/legends/$key"] = legendValues
        childUpdates["/user-legends/$uid/$key"] = legendValues
        app.database.updateChildren(childUpdates)
        hideLoader(loader)
    }

    private fun setButtonListener(layout: View) {
        layout.addLegendButton.setOnClickListener {
            val legendName = lName.text.toString()
            val caps = caps.text.toString()
            val trophiesWon = trophiesWon.text.toString()
            val yrsAtClub = yrsAtClub.text.toString()
            when {
                layout.lName.text.isEmpty() -> Toast.makeText(app, R.string.error_lname, Toast.LENGTH_LONG).show()
                layout.caps.text.isEmpty() -> Toast.makeText(app, R.string.error_caps, Toast.LENGTH_LONG).show()
                layout.trophiesWon.text.isEmpty() -> Toast.makeText(app, R.string.error_trophiesWon, Toast.LENGTH_LONG).show()
                layout.yrsAtClub.text.isEmpty() -> Toast.makeText(app, R.string.error_yrsAtClub, Toast.LENGTH_LONG).show()
                else -> {
                    writeNewLegend(
                        LegendModel(
                            legendName = legendName,
                            caps = caps,
                            trophiesWon = trophiesWon,
                            yrsAtClub =yrsAtClub,
                            email = app.currentUser.email
                        )
                    )
                }
            }
            layout.lName.setText("").toString()
            layout.caps.setText("").toString()
            layout.trophiesWon.setText("").toString()
            layout.yrsAtClub.setText("").toString()

        }
    }
}
