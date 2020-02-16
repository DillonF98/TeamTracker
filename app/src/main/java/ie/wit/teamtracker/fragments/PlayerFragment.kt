package ie.wit.teamtracker.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ie.wit.R
import ie.wit.teamtracker.main.PlayerApp
import ie.wit.teamtracker.models.PlayerModel
import kotlinx.android.synthetic.main.fragment_player.*
import kotlinx.android.synthetic.main.fragment_player.view.*


class PlayerFragment : Fragment() {

    lateinit var app: PlayerApp
    var player = PlayerModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as PlayerApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_player, container, false)
        activity?.title = getString(R.string.action_player)
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

    private fun setButtonListener(layout: View) {

        layout.addPlayerButton.setOnClickListener {

            player.name = pName.text.toString()
            player.position = pPosition.text.toString()
            player.age = pAge.text.toString()
            player.yearSigned = pYearSigned.text.toString()
            player.signingValue = pSigningValue.text.toString()
            player.currentValue = pCurrentValue.text.toString()
            player.nationality = pNationality.text.toString()

            if (player.name.isNotEmpty() && player.position.isNotEmpty() && player.age.isNotEmpty()&& player.yearSigned.isNotEmpty() && player.signingValue.isNotEmpty()
                && player.currentValue.isNotEmpty() && player.nationality.isNotEmpty()) {

                app.playerStore.create(player.copy())
            }
            else {
                Toast.makeText(app, R.string.error_text, Toast.LENGTH_LONG).show()
            }


                player.name = pName.setText("").toString()
                player.age = pAge.setText("").toString()
                player.position = pPosition.setText("").toString()
                player.yearSigned = pYearSigned.setText("").toString()
                player.signingValue = pSigningValue.setText("").toString()
                player.currentValue = pCurrentValue.setText("").toString()
                player.nationality = pNationality.setText("").toString()

            }
        }
    }

