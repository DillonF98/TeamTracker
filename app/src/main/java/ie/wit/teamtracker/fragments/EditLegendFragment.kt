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
import ie.wit.teamtracker.models.LegendModel
import ie.wit.teamtracker.utils.createLoader
import ie.wit.teamtracker.utils.hideLoader
import ie.wit.teamtracker.utils.showLoader
import kotlinx.android.synthetic.main.fragment_edit_legend.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class EditLegendFragment : Fragment(), AnkoLogger {

    lateinit var app: PlayerApp
    lateinit var loader : AlertDialog
    lateinit var root: View
    var editLegend: LegendModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as PlayerApp

        arguments?.let {
            editLegend = it.getParcelable("editlegend")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_edit_legend, container, false)
        activity?.title = getString(R.string.action_legend_edit)
        loader = createLoader(activity!!)

        //root.editLName.setText(editLegend!!.legendName)
//        root.editCaps.setText(editLegend!!.caps)
//        root.editTrophiesWon.setText(editLegend!!.trophiesWon)
//        root.editYrsAtClub.setText(editLegend!!.yrsAtClub)



        root.editLegendButton.setOnClickListener {
            showLoader(loader, "Updating Legend on Server...")
            updateLegendData()
            updateLegend(editLegend!!.uid, editLegend!!)
            updateUserLegend(app.auth.currentUser!!.uid,
                editLegend!!.uid, editLegend!!)
        }

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(Legend: LegendModel) =
            EditLegendFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("editlegendr",Legend)
                }
            }
    }

    fun updateLegendData() {
        editLegend!!.legendName = root.editLName.text.toString()
        editLegend!!.caps = root.editCaps.text.toString()
        editLegend!!.yrsAtClub = root.editYrsAtClub.text.toString()
        editLegend!!.trophiesWon = root.editTrophiesWon.text.toString()

    }

    fun updateUserLegend(userId: String, uid: String?, Legend: LegendModel) {
        app.database.child("user-legends").child(userId).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(Legend)
                        activity!!.supportFragmentManager.beginTransaction()
                            .replace(R.id.homeFrame, LegendListFragment.newInstance())
                            .addToBackStack(null)
                            .commit()
                        hideLoader(loader)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Legend error : ${error.message}")
                    }
                })
    }

    fun updateLegend(uid: String?, legend: LegendModel) {
        app.database.child("legends").child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(legend)
                        hideLoader(loader)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Legend error : ${error.message}")
                    }
                })
    }
}
