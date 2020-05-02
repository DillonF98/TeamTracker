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
import ie.wit.teamtracker.models.PlayerModel
import ie.wit.teamtracker.utils.createLoader
import ie.wit.teamtracker.utils.hideLoader
import ie.wit.teamtracker.utils.showLoader
import kotlinx.android.synthetic.main.fragment_player.*
import kotlinx.android.synthetic.main.fragment_player.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class PlayerFragment : Fragment(), AnkoLogger {

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

        val root = inflater.inflate(R.layout.fragment_player, container, false)
        loader = createLoader(activity!!)
        activity?.title = getString(R.string.action_legend_edit)

        setButtonListener(root)

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PlayerFragment().apply {
                arguments = Bundle().apply {}
            }
    }


    fun writeNewPlayer(player: PlayerModel) {

        // Create new player at /players & /players/$uid
        showLoader(loader, "Adding Player to Firebase")
        info("Firebase DB Reference : $app.database")
        val uid = app.auth.currentUser!!.uid
        val key = app.database.child("players").push().key
        if (key == null) {
            info("Firebase Error : Key Empty")
            return
        }
        player.uid = key
        val playerValues = player.toMap()

        val childUpdates = HashMap<String, Any>()
        childUpdates["/players/$key"] = playerValues
        childUpdates["/user-players/$uid/$key"] = playerValues

        app.database.updateChildren(childUpdates)
        hideLoader(loader)
    }

    private fun setButtonListener(layout: View) {

        layout.addPlayerButton.setOnClickListener {

            val name = pName.text.toString()
            val position = pPosition.text.toString()
            val age = pAge.text.toString()
            val yearSigned = pYearSigned.text.toString()
            val signingValue = pSigningValue.text.toString()
            val currentValue = pCurrentValue.text.toString()
            val nationality = pNationality.text.toString()
            val uid = pNationality.text.toString()


            when {
                layout.pName.text.isEmpty() -> Toast.makeText(app, R.string.error_name, Toast.LENGTH_LONG).show()
                layout.pPosition.text.isEmpty() -> Toast.makeText(app, R.string.error_position, Toast.LENGTH_LONG).show()
                layout.pAge.text.isEmpty() -> Toast.makeText(app, R.string.error_Age, Toast.LENGTH_LONG).show()
                layout.pYearSigned.text.isEmpty() -> Toast.makeText(app, R.string.error_yearSigned, Toast.LENGTH_LONG).show()
                layout.pSigningValue.text.isEmpty() -> Toast.makeText(app, R.string.error_signingValue, Toast.LENGTH_LONG).show()
                layout.pCurrentValue.text.isEmpty() -> Toast.makeText(app, R.string.error_currentValue, Toast.LENGTH_LONG).show()
                layout.pNationality.text.isEmpty() -> Toast.makeText(app, R.string.error_nationality, Toast.LENGTH_LONG).show()

                else -> {
                    writeNewPlayer(
                        PlayerModel(
                            name = name,
                            age = age,
                            yearSigned = yearSigned,
                            currentValue = currentValue,
                            signingValue = signingValue,
                            position = position,
                            nationality = nationality,
                            email = app.auth.currentUser?.email
                        )
                    )
                }
            }

            layout.pName.setText("")
            layout.pAge.setText("")
            layout.pPosition.setText("")
            layout.pYearSigned.setText("")
            layout.pSigningValue.setText("")
            layout.pCurrentValue.setText("")
            layout.pNationality.setText("")

        }
    }
}



