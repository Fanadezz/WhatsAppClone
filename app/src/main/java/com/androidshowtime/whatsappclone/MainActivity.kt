package com.androidshowtime.whatsappclone

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var drawer:DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())

        //initialize drawer
        drawer = findViewById(R.id.drawerLayout)

        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer)
        NavigationUI.setupWithNavController(navView, navController)


        // // prevent nav gesture if not on start destination

        navController.addOnDestinationChangedListener { controller, destination, arguments ->


            if (destination.id == controller.graph.startDestination ){

                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

            }

            else{

                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        val navController = findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, drawer)
    }
}