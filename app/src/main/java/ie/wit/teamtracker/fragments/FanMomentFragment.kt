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
import ie.wit.teamtracker.models.FanMomentModel
import ie.wit.teamtracker.utils.createLoader
import ie.wit.teamtracker.utils.hideLoader
import ie.wit.teamtracker.utils.showLoader
import kotlinx.android.synthetic.main.fragment_fan_moment.*
import kotlinx.android.synthetic.main.fragment_fan_moment.view.*
import kotlinx.android.synthetic.main.fragment_fan_moment.view.fDate
import kotlinx.android.synthetic.main.fragment_fan_moment.view.fDesc
import kotlinx.android.synthetic.main.fragment_fan_moment.view.fName
import kotlinx.android.synthetic.main.fragment_fan_moment.view.fTitle
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class FanMomentFragment : Fragment(), AnkoLogger {

    lateinit var app: PlayerApp
    lateinit var loader: AlertDialog
    var favourite = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as PlayerApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_fan_moment, container, false)
        loader = createLoader(activity!!)
        activity?.title = getString(R.string.action_fan_moment)

        setButtonListener(root)
        setFavouriteListener(root)

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FanMomentFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    fun setFavouriteListener (layout: View) {
        layout.imagefavourite.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (!favourite) {
                    layout.imagefavourite.setImageResource(android.R.drawable.star_big_on)
                    favourite = true
                }
                else {
                    layout.imagefavourite.setImageResource(android.R.drawable.star_big_off)
                    favourite = false
                }
            }
        })
    }


    fun writeNewFanMoment(fanMoment: FanMomentModel) {

        showLoader(loader, "Adding Fan Moment to Firebase")
        info("Firebase DB Reference : $app.database")
        val uid = app.currentUser.uid
        val key = app.database.child("fanmoments").push().key
        if (key == null) {
            info("Firebase Error : Key Empty")
            return
        }
        fanMoment.uid = key
        val fanMomentValues = fanMoment.toMap()

        val childUpdates = HashMap<String, Any>()
        childUpdates["/fan-moments/$key"] = fanMomentValues
        childUpdates["/user-fan-moments/$uid/$key"] = fanMomentValues

        app.database.updateChildren(childUpdates)
        hideLoader(loader)
    }

    private fun setButtonListener(layout: View) {

        layout.addFanButton.setOnClickListener {

            val name = fName.text.toString()
            val title = fTitle.text.toString()
            val desc = fDesc.text.toString()
            val date = fDate.text.toString()

            when {
                layout.fName.text.isEmpty() -> Toast.makeText(app, R.string.error_name, Toast.LENGTH_LONG).show()
                layout.fTitle.text.isEmpty() -> Toast.makeText(app, R.string.error_title, Toast.LENGTH_LONG).show()
                layout.fDate.text.isEmpty() -> Toast.makeText(app, R.string.error_date, Toast.LENGTH_LONG).show()
                layout.fDesc.text.isEmpty() -> Toast.makeText(app, R.string.error_desc, Toast.LENGTH_LONG).show()

                else -> {
                    writeNewFanMoment(
                        FanMomentModel(
                            name = name,
                            title = title,
                            desc = desc,
                            date = date,
                            isfavourite = favourite,
                            latitude = app.currentLocation.latitude,
                            longitude = app.currentLocation.longitude,
                            email = app.currentUser.email
                        )
                    )
                }
            }
            layout.fName.setText("")
            layout.fTitle.setText("")
            layout.fDate.setText("")
            layout.fDesc.setText("")
        }
    }
}



