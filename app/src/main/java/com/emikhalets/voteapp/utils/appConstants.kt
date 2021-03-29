package com.emikhalets.voteapp.utils

import android.Manifest
import com.emikhalets.voteapp.BuildConfig
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.model.firebase.FirebaseAuthRepository
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import com.emikhalets.voteapp.model.firebase.fillCurrentUserData
import com.emikhalets.voteapp.view.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber

lateinit var ACTIVITY: MainActivity
lateinit var AUTH: FirebaseAuth
lateinit var USER_ID: String
lateinit var USER: User

lateinit var AUTH_REPOSITORY: FirebaseAuthRepository
lateinit var DATABASE_REPOSITORY: FirebaseDatabaseRepository

const val CAMERA = Manifest.permission.CAMERA

fun firstInitializationApp() {
    initAuthentication()
    initRepositories()
    initLogger()
}

fun initAuthentication() {
    AUTH = Firebase.auth
    USER_ID = AUTH.currentUser?.uid.toString()
    USER = User(id = USER_ID)
    fillCurrentUserData()
}

private fun initRepositories() {
    AUTH_REPOSITORY = FirebaseAuthRepository()
    DATABASE_REPOSITORY = FirebaseDatabaseRepository()
}

private fun initLogger() {
    if (BuildConfig.DEBUG) Timber.plant(object : Timber.DebugTree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            super.log(priority, "___APPLICATION___", message, t)
        }
    })
}

