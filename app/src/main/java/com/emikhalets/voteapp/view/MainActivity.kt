package com.emikhalets.voteapp.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private var navController: NavController? = null
    private var appBar: AppBarConfiguration? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setSupportActionBar(binding!!.toolbar)
        navController = Navigation.findNavController(this, R.id.main_nav_host)
        appBar = AppBarConfiguration.Builder(navController!!.graph).build()
        NavigationUI.setupWithNavController(binding!!.toolbar, navController!!, appBar!!)
        binding!!.navigationView.setNavigationItemSelectedListener { item: MenuItem -> navigationListener(item) }
        navController!!.addOnDestinationChangedListener { controller: NavController, destination: NavDestination, arguments: Bundle? -> destinationChanged(controller, destination, arguments!!) }
        when (navController!!.currentDestination!!.id) {
            R.id.logoFragment, R.id.authLoginFragment, R.id.authRegisterFragment -> binding!!.toolbar.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController!!, appBar!!) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val currentFragment = navController!!.currentDestination!!.id
        when (currentFragment) {
            R.id.authLoginFragment, R.id.homeFragment -> finish()
            else -> navController!!.popBackStack()
        }
    }

    private fun navigationListener(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_nav_profile -> navController!!.navigate(R.id.action_homeFragment_to_profileFragment)
            R.id.menu_nav_user_images -> navController!!.navigate(R.id.action_homeFragment_to_userImagesFragment)
            R.id.menu_nav_voting -> navController!!.navigate(R.id.action_homeFragment_to_votingFragment)
            R.id.menu_nav_top_images -> navController!!.navigate(R.id.action_homeFragment_to_topImagesFragment)
            R.id.menu_nav_top_users -> navController!!.navigate(R.id.action_homeFragment_to_topUsersFragment)
        }
        NavigationUI.onNavDestinationSelected(item, navController!!)
        binding!!.layoutDrawer.closeDrawers()
        return true
    }

    private fun destinationChanged(controller: NavController,
                                   destination: NavDestination,
                                   arguments: Bundle) {
        if (destination.id == R.id.homeFragment) {
            binding!!.layoutDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        } else {
            binding!!.layoutDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }
}