package com.example.products.activities

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.products.R
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration;
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout) // get the reference to the drawer :P

        val toolbar: Toolbar? = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar) //set the toolbar

        val host: NavHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
        val navController = host.navController

        appBarConfiguration = AppBarConfiguration(navController.graph) //configure nav controller

        setupNavigation(navController) //setup navigation

        setupActionBar(navController, appBarConfiguration) // setup action bar

        //hear for event changes
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                destination.id.toString()
            }
        }
    }
    private fun setupActionBar(
            navController: NavController,
            appBarConfig: AppBarConfiguration
    ) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    private fun setupNavigation(navController: NavController) {
        val sideNavView = findViewById<NavigationView>(R.id.nav_view)
        sideNavView?.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
                setOf(R.id.fragment_main, R.id.fragment_insert),
                drawerLayout)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val retValue = super.onCreateOptionsMenu(menu)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        if (navigationView == null) {
            menuInflater.inflate(R.menu.main_drawer, menu)
            return true
        }
        return retValue
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // I need to open the drawer onClick
        when (item.itemId) {
            android.R.id.home ->
                drawerLayout.openDrawer(GravityCompat.START)
        }
        return item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}