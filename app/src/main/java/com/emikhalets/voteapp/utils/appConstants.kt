package com.emikhalets.voteapp.utils

import android.Manifest
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.model.firebase.FirebaseAuthRepository
import com.emikhalets.voteapp.model.firebase.FirebaseDatabaseRepository
import com.emikhalets.voteapp.model.firebase.FirebaseStorageRepository
import com.emikhalets.voteapp.view.MainActivity

lateinit var ACTIVITY: MainActivity
var USER: User = User()

//val AUTH_REPOSITORY = FirebaseAuthRepository()
//val STORAGE_REPOSITORY = FirebaseStorageRepository()
//val DATABASE_REPOSITORY = FirebaseDatabaseRepository()

const val CAMERA = Manifest.permission.CAMERA

const val ARGS_PHOTO = "arguments_photo"
const val ARGS_NAME = "arguments_name"
const val ARGS_POS = "arguments_position"