package ie.wit.teamtracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ie.wit.R
import ie.wit.teamtracker.adapters.PlayerAdapter
import ie.wit.teamtracker.adapters.TrophyAdapter
import ie.wit.teamtracker.main.PlayerApp
import ie.wit.teamtracker.models.PlayerModel
import ie.wit.teamtracker.models.TrophyModel
import ie.wit.teamtracker.utils.*
import kotlinx.android.synthetic.main.fragment_player.view.*
import kotlinx.android.synthetic.main.fragment_player_list.view.*
import kotlinx.android.synthetic.main.fragment_trophy.*
import kotlinx.android.synthetic.main.fragment_trophy.view.*
import kotlinx.android.synthetic.main.fragment_trophy_list.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class TrophyFragment : Fragment(), AnkoLogger {

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

        val root = inflater.inflate(R.layout.fragment_trophy, container, false)
        activity?.title = getString(R.string.action_addTrophy)
        loader = createLoader(activity!!)

        setButtonListener(root)

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TrophyFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    private fun setButtonListener(layout: View) {

        layout.addTrophyButton.setOnClickListener {

            val trophyName = tName.text.toString()
            val trophyAmount = tAmount.text.toString()

            when {
                layout.tName.text.isEmpty() -> Toast.makeText(app, R.string.error_tname, Toast.LENGTH_LONG).show()
                layout.tAmount.text.isEmpty() -> Toast.makeText(app, R.string.error_tAmount, Toast.LENGTH_LONG).show()


                else -> {
                    writeNewTrophy(
                        TrophyModel(
                            trophyName = trophyName,
                            trophyAmount = trophyAmount,
                            email = app.currentUser.email
                        )
                    )
                }
            }


            layout.tName.setText("")
            layout.tAmount.setText("")

        }
    }


    fun writeNewTrophy(trophy: TrophyModel) {

        // Create new trophy at /trophys & /trophys/$uid
        showLoader(loader, "Adding Trophy to Firebase")
        info("Firebase DB Reference : $app.database")
        val uid = app.currentUser.uid
        val key = app.database.child("trophys").push().key
        if (key == null) {
            info("Firebase Error : Key Empty")
            return
        }
        trophy.uid = key
        val trophyValues = trophy.toMap()

        val childUpdates = HashMap<String, Any>()
        childUpdates["/trophys/$key"] = trophyValues
        childUpdates["/user-trophys/$uid/$key"] = trophyValues

        app.database.updateChildren(childUpdates)
        hideLoader(loader)
    }

}
