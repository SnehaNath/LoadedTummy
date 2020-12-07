package com.intershala.loadedtummy.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.intershala.loadedtummy.R
import com.intershala.loadedtummy.fragment.*
import com.intershala.loadedtummy.util.SessionManager
import kotlinx.android.synthetic.main.activity_main_home.view.*

class MainHomeActivity : AppCompatActivity() {

    lateinit var drawerLayout : DrawerLayout
    lateinit var coordinatorLayout : CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frame : FrameLayout
    lateinit var navigationView : NavigationView
    lateinit var headerName : TextView
    lateinit var headerNo : TextView

    lateinit var sessionManager : SessionManager
    lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_home)

        sessionManager = SessionManager(this)
        sharedPreferences = getSharedPreferences(sessionManager.PREF_NAME, Context.MODE_PRIVATE)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frame = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)
//        headerName = findViewById(R.id.txtHeaderName)
//        headerNo = findViewById(R.id.txtHeaderNo)
        

        setUpToolbar()

        openHome()
        

        val actionBarDrawerToggle = ActionBarDrawerToggle(this@MainHomeActivity, drawerLayout, R.string.open_drawer, R.string.close_drawer)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

/*            e.text = sharedPreferences.getString("user_id","a")*/

        navigationView.setNavigationItemSelectedListener {



            when(it.itemId) {
                R.id.home -> {
                    openHome()
                    drawerLayout.closeDrawers()
                }
                R.id.userProfile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, MyProfileFragment())
                        .commit()

                    supportActionBar?.title = "My Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.favoriteRestaurant -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FavoriteRestroFragment())
                        .commit()

                    supportActionBar?.title = "Favorite Restaurants"
                    drawerLayout.closeDrawers()
                }
                R.id.orderHistory -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, OrderHistoryFragment())
                        .commit()

                    supportActionBar?.title = "My Previous Orders"
                    drawerLayout.closeDrawers()
                }
                R.id.FAQs -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FAQsFragment())
                        .commit()

                    supportActionBar?.title = "Frequently Asked Questions"
                    drawerLayout.closeDrawers()
                }
                R.id.logout -> {
                    drawerLayout.closeDrawers()
                    val dialog = AlertDialog.Builder(this@MainHomeActivity)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Are you sure you want to log out?")
                    dialog.setPositiveButton("Yes") { text, listener ->
                        sessionManager.setLogin(false)
                        startActivity(Intent(this@MainHomeActivity, Login::class.java))
                        finish()                                    //------------------------------------don't add this after making home and favourite and see what happens
                    }
                    dialog.setNegativeButton("No") { text, listener ->
                    }
                    dialog.create()
                    dialog.show()
                }

            }
            return@setNavigationItemSelectedListener true
        }

    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == android.R.id.home)
            drawerLayout.openDrawer(GravityCompat.START)
        return super.onOptionsItemSelected(item)
    }

    fun openHome() {
        val fragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment).commit()

        supportActionBar?.title = "All Restaurants"
        navigationView.setCheckedItem(R.id.home)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)
        when(frag) {
            !is HomeFragment -> openHome()
            else -> super.onBackPressed()
        }
    }
}
