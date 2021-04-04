package com.emikhalets.voteapp.utils

import android.Manifest
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.view.MainActivity

lateinit var ACTIVITY: MainActivity
lateinit var USER: User

const val CAMERA = Manifest.permission.CAMERA

const val ARGS_PHOTO = "arguments_photo"
const val ARGS_NAME = "arguments_name"
const val ARGS_POS = "arguments_position"