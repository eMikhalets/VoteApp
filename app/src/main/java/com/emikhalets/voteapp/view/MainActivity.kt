package com.emikhalets.voteapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.emikhalets.voteapp.BuildConfig
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.ActivityMainBinding
import com.emikhalets.voteapp.model.firebase.AUTH
import com.emikhalets.voteapp.model.firebase.initAuth
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.utils.navigate
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    lateinit var drawer: AppDrawer
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ACTIVITY = this
        init()
    }

    override fun onResume() {
        super.onResume()
        if (AUTH.currentUser == null) navigate(R.id.action_home_to_authLogin)
    }

    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
            R.id.authLoginFragment -> finish()
            else -> super.onBackPressed()
        }
    }

    private fun init() {
        initAuth()
        initLogger()
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        drawer = AppDrawer()
        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        drawer.create()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) Timber.plant(object : Timber.DebugTree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                super.log(priority, "___APPLICATION___", message, t)
            }
        })
    }
}