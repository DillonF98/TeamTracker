package ie.wit.teamtracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ie.wit.R
import ie.wit.teamtracker.main.PlayerApp
import ie.wit.teamtracker.models.TrophyModel
import kotlinx.android.synthetic.main.fragment_trophy.*
import kotlinx.android.synthetic.main.fragment_trophy.view.*


class TrophyFragment : Fragment() {

    lateinit var app: PlayerApp
    var trophy = TrophyModel()


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
        setTrophyButtonListener(root)

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TrophyFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    private fun setTrophyButtonListener(layout: View) {

        layout.addTrophyButton.setOnClickListener {

            trophy.trophyName = tName.text.toString()
            trophy.trophyAmount = tAmount.text.toString()

            if (trophy.trophyName.isNotEmpty()){

                app.trophyStore.create(trophy.copy())
            }

            else{
                Toast.makeText(app, R.string.error_trophyText, Toast.LENGTH_LONG).show()
            }

            trophy.trophyName = tName.setText("").toString()
            trophy.trophyAmount = tAmount.setText("").toString()

        }
    }

}
