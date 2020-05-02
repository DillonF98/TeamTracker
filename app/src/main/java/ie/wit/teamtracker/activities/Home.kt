package ie.wit.teamtracker.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import ie.wit.R
import ie.wit.teamtracker.fragments.PlayersAllFragment
import ie.wit.teamtracker.fragments.*
import ie.wit.teamtracker.main.PlayerApp
import ie.wit.teamtracker.utils.*
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var ft: FragmentTransaction
    lateinit var app: PlayerApp


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        setSupportActionBar(toolbar)

        app = application as PlayerApp

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Corner Taken Quickly ORIGI!!!!!!!!!",
                Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }

        navView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if(app.currentUser.email != null)
            navView.getHeaderView(0).nav_header_email.text = app.currentUser.email
        else
            navView.getHeaderView(0).nav_header_email.text = "No Email Specified..."

        if(app.currentUser.displayName != null)
            navView.getHeaderView(0).nav_header_name.text = app.currentUser.displayName
            else
            navView.getHeaderView(0).nav_header_name.text = "No Name Specified..."

        //Checking if Google User, upload google profile pic
        checkExistingPhoto(app,this)

        navView.getHeaderView(0).imageView
            .setOnClickListener { showImagePicker(this,1) }

        ft = supportFragmentManager.beginTransaction()

        val fragment = HomeFragment.newInstance()
        ft.replace(R.id.homeFrame, fragment)
        ft.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.nav_home-> navigateTo(HomeFragment.newInstance())
            R.id.nav_player -> navigateTo(PlayerFragment.newInstance())
            R.id.nav_players -> navigateTo(PlayerListFragment.newInstance())
            R.id.nav_players_all -> navigateTo(PlayersAllFragment.newInstance())
            R.id.nav_history -> navigateTo(HistoryFragment.newInstance())
            R.id.nav_addTrophies-> navigateTo(TrophyFragment.newInstance())
            R.id.nav_trophies -> navigateTo(TrophyListFragment.newInstance())
            R.id.nav_addLegends -> navigateTo(LegendFragment.newInstance())
            R.id.nav_legends -> navigateTo(LegendListFragment.newInstance())
            R.id.nav_sign_out -> signOut()

            else -> toast("You Selected Something Else")
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    private fun navigateTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun signOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener { startActivity<Login>() }
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (data != null) {
                    writeImageRef(app,readImageUri(resultCode, data).toString())
                    Picasso.get().load(readImageUri(resultCode, data).toString())
                        .resize(180, 180)
                        .transform(CropCircleTransformation())
                        .into(navView.getHeaderView(0).imageView, object : Callback {
                            override fun onSuccess() {
                                // Drawable is ready
                                uploadImageView(app,navView.getHeaderView(0).imageView)
                            }
                            override fun onError(e: Exception) {}
                        })
                }
            }
        }
    }
}
