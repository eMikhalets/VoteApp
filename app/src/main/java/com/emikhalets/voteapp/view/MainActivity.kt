package com.emikhalets.voteapp.view

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.ActivityMainBinding
import com.emikhalets.voteapp.utils.CAMERA
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    lateinit var drawer: AppDrawer
    lateinit var toolbar: Toolbar

    private val permissionResult = registerForActivityResult(RequestPermission()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AndroidInjection.inject(this)
        initNavigation()
        initViews()
        permissionResult.launch(CAMERA)
    }

    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
            R.id.authLoginFragment -> finish()
            R.id.homeFragment -> finish()
            else -> super.onBackPressed()
        }
    }

    private fun initNavigation() {
        val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun initViews() {
        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        drawer = AppDrawer(this)
        drawer.create()
    }
}