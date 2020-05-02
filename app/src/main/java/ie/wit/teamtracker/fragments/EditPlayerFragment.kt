package ie.wit.teamtracker.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ie.wit.R
import ie.wit.teamtracker.main.PlayerApp
import ie.wit.teamtracker.models.PlayerModel
import ie.wit.teamtracker.utils.createLoader
import ie.wit.teamtracker.utils.hideLoader
import ie.wit.teamtracker.utils.showLoader
import kotlinx.android.synthetic.main.fragment_player_edit.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class EditPlayerFragment : Fragment(), AnkoLogger {

    lateinit var app: PlayerApp
    lateinit var loader : AlertDialog
    lateinit var root: View
    var editPlayer: PlayerModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as PlayerApp

        arguments?.let {
            editPlayer = it.getParcelable("editplayer")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_player_edit, container, false)
        activity?.title = getString(R.string.action_player_edit)
        loader = createLoader(activity!!)

        root.editPName.setText(editPlayer!!.name)
        root.editPAge.setText(editPlayer!!.age)
        root.editPPosition.setText(editPlayer!!.position)
        root.editPNationality.setText(editPlayer!!.nationality)
        root.editPCurrentValue.setText(editPlayer!!.currentValue)
        root.editPSigningValue.setText(editPlayer!!.signingValue)
        root.editPYearSigned.setText(editPlayer!!.yearSigned)

        root.editPlayerButton.setOnClickListener {
            showLoader(loader, "Updating Player on Server...")
            updatePlayerData()
            updatePlayer(editPlayer!!.uid, editPlayer!!)
            updateUserPlayer(app.auth.currentUser!!.uid,
                               editPlayer!!.uid, editPlayer!!)
        }

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(Player: PlayerModel) =
            EditPlayerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("editplayer",Player)
                }
            }
    }

    fun updatePlayerData() {
        editPlayer!!.name = root.editPName.text.toString()
        editPlayer!!.age = root.editPAge.text.toString()
        editPlayer!!.position = root.editPPosition.text.toString()
        editPlayer!!.nationality = root.editPNationality.text.toString()
        editPlayer!!.currentValue = root.editPCurrentValue.text.toString()
        editPlayer!!.yearSigned = root.editPYearSigned.text.toString()
        editPlayer!!.signingValue = root.editPSigningValue.text.toString()
    }

    fun updateUserPlayer(userId: String, uid: String?, Player: PlayerModel) {
        app.database.child("user-players").child(userId).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(Player)
                        activity!!.supportFragmentManager.beginTransaction()
                        .replace(R.id.homeFrame, PlayerListFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                        hideLoader(loader)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Player error : ${error.message}")
                    }
                })
    }

    fun updatePlayer(uid: String?, Player: PlayerModel) {
        app.database.child("players").child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(Player)
                        hideLoader(loader)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Player error : ${error.message}")
                    }
                })
    }
}
