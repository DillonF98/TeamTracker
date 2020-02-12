package ie.wit.teamtracker.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ie.wit.R
import ie.wit.teamtracker.main.PlayerApp
import ie.wit.teamtracker.models.PlayerModel
import kotlinx.android.synthetic.main.card_player.*
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

    fun setButtonListener( layout: View) {
        layout.addPlayerButton.setOnClickListener {
            player.name = playerName.text.toString()
            player.position = pPosition.text.toString()
            //player.age = age.text.toString()
            //player.yearSigned = yearSigned.text.toString()
            //player.signingvalue = pSigningValue.text.toString()
            //player.currentvalue = pCurrentValue.text.toString()
            //player.nationality = pNationality.text.toString()

                app.playerStore.create(player.copy())

            player.name = playerName.setText("").toString()
            player.age = pPosition.setText("").toString()

        }
        }



}
