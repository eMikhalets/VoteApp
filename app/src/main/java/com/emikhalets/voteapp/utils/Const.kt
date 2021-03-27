package com.emikhalets.voteapp.utils

import android.Manifest

object Const {
    const val BASE_URL = "http://s1.ntech.team:8082"

    // SharedPreferences
    const val SHARED_FILE = "shared_fourtop"
    const val SHARED_TOKEN = "shared_user_token"

    // Bundle
    const val ARGS_TOKEN = "args_user_token"
    const val ARGS_LOGIN = "args_login"
    const val ARGS_PASS = "args_password"

    // Permissions
    const val READ_EXTERNAL_REQUEST = 1
    val READ_EXTERNAL_PERMISSION = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
}