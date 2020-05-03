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
import ie.wit.teamtracker.models.FanMomentModel
import ie.wit.teamtracker.utils.createLoader
import ie.wit.teamtracker.utils.hideLoader
import ie.wit.teamtracker.utils.showLoader
import kotlinx.android.synthetic.main.fragment_fan_moment_edit.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class EditFanMomentFragment : Fragment(), AnkoLogger {

    lateinit var app: PlayerApp
    lateinit var loader : AlertDialog
    lateinit var root: View
    var editFanMoment: FanMomentModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as PlayerApp

        arguments?.let {
            editFanMoment = it.getParcelable("editfanmoment")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_fan_moment_edit, container, false)
        activity?.title = getString(R.string.action_fan_moment_edit)
        loader = createLoader(activity!!)

        root.editFTitle.setText(editFanMoment!!.title)
        root.editFName.setText(editFanMoment!!.name)
        root.editFDate.setText(editFanMoment!!.date)
        root.editFDesc.setText(editFanMoment!!.desc)

        root.editFanButton.setOnClickListener {
            showLoader(loader, "Updating Fan Moment on Server...")
            updateFanMomentData()
            updateFanMoment(editFanMoment!!.uid, editFanMoment!!)
            updateUserFanMoment(
                app.currentUser.uid,
                               editFanMoment!!.uid, editFanMoment!!)
        }

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(fanMoment: FanMomentModel) =
            EditFanMomentFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("editfanmoment",fanMoment)
                }
            }
    }

    fun updateFanMomentData() {
        editFanMoment!!.name = root.editFName.text.toString()
        editFanMoment!!.title = root.editFTitle.text.toString()
        editFanMoment!!.date = root.editFDate.text.toString()
        editFanMoment!!.desc = root.editFDesc.text.toString()

    }

    fun updateUserFanMoment(userId: String, uid: String?, FanMoment: FanMomentModel) {
        app.database.child("user-fan-moments").child(userId).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(FanMoment)
                        activity!!.supportFragmentManager.beginTransaction()
                        .replace(R.id.homeFrame, FanMomentListFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                        hideLoader(loader)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Fan Moment error : ${error.message}")
                    }
                })
    }

    fun updateFanMoment(uid: String?, FanMoment: FanMomentModel) {
        app.database.child("fan-moments").child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(FanMoment)
                        hideLoader(loader)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Fan Moment error : ${error.message}")
                    }
                })
    }
}
