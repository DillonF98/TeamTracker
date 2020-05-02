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
import ie.wit.teamtracker.models.TrophyModel
import ie.wit.teamtracker.utils.createLoader
import ie.wit.teamtracker.utils.hideLoader
import ie.wit.teamtracker.utils.showLoader
import kotlinx.android.synthetic.main.fragment_edit_trophy.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class EditTrophyFragment : Fragment(), AnkoLogger {

    lateinit var app: PlayerApp
    lateinit var loader : AlertDialog
    lateinit var root: View
    var editTrophy: TrophyModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as PlayerApp

        arguments?.let {
            editTrophy = it.getParcelable("edittrophy")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_edit_trophy, container, false)
        activity?.title = getString(R.string.action_trophy_edit)
        loader = createLoader(activity!!)

        root.editTName.setText(editTrophy!!.trophyName)
        root.editTAmount.setText(editTrophy!!.trophyAmount)

        root.editTrophyButton.setOnClickListener {
            showLoader(loader, "Updating Trophy on Server...")
            updateTrophyData()
            updateTrophy(editTrophy!!.uid, editTrophy!!)
            updateUserTrophy(app.auth.currentUser!!.uid,
                editTrophy!!.uid, editTrophy!!)
        }

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(Trophy: TrophyModel) =
            EditTrophyFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("edittrophy",Trophy)
                }
            }
    }

    fun updateTrophyData() {
        editTrophy!!.trophyName= root.editTName.text.toString()
        editTrophy!!.trophyAmount = root.editTAmount.text.toString()

    }

    fun updateUserTrophy(userId: String, uid: String?, Trophy: TrophyModel) {
        app.database.child("user-trophys").child(userId).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(Trophy)
                        activity!!.supportFragmentManager.beginTransaction()
                            .replace(R.id.homeFrame, TrophyListFragment.newInstance())
                            .addToBackStack(null)
                            .commit()
                        hideLoader(loader)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Trophy error : ${error.message}")
                    }
                })
    }

    fun updateTrophy(uid: String?, Trophy: TrophyModel) {
        app.database.child("trophys").child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(Trophy)
                        hideLoader(loader)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Trophy error : ${error.message}")
                    }
                })
    }
}
