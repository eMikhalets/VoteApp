package com.emikhalets.voteapp.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

lateinit var AUTH: FirebaseAuth
lateinit var USER_ID: String
var USER: FirebaseUser? = null

fun initAuthentication() {
    AUTH = Firebase.auth
    USER = AUTH.currentUser
    USER_ID = USER?.uid ?: ""
}

//fun checkUserExisting(onComplete: () -> Unit) {
//    FirebaseDatabase.getInstance().reference.child("users").child(USER_ID).singleDataChange {
//        if (!it.exists()) onComplete()
//    }
//}