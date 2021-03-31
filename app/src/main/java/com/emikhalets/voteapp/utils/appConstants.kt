package com.emikhalets.voteapp.utils

import android.Manifest
import com.emikhalets.voteapp.model.firebase.FirebaseAuthRepository
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import com.emikhalets.voteapp.model.firebase.FirebaseStorageRepository
import com.emikhalets.voteapp.view.MainActivity

lateinit var ACTIVITY: MainActivity
lateinit var USER_ID: String
lateinit var USERNAME: String
lateinit var USER_PHOTO: String

lateinit var AUTH_REPOSITORY: FirebaseAuthRepository
lateinit var STORAGE_REPOSITORY: FirebaseStorageRepository
lateinit var DATABASE_REPOSITORY: FirebaseDatabaseRepository

const val CAMERA = Manifest.permission.CAMERA

const val ARGS_PHOTO = "arguments_photo"