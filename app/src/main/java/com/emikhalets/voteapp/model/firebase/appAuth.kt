package com.emikhalets.voteapp.model.firebase

import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.utils.popBackStack
import com.emikhalets.voteapp.utils.toastException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

lateinit var AUTH: FirebaseAuth
lateinit var USER_ID: String
lateinit var USER: User

fun initAuth() {
    AUTH = Firebase.auth
    USER = User()
    USER_ID = AUTH.currentUser?.uid.toString()
}

fun sendLoginToFirebase(login: String, pass: String) {
    val email = ACTIVITY.getString(R.string.app_login_to_email, login)
    AUTH.signInWithEmailAndPassword(email, pass)
            .addOnFailureListener { toastException(it) }
            .addOnSuccessListener {
                USER_ID = AUTH.currentUser?.uid.toString()
                popBackStack(R.id.homeFragment)
            }
}

fun sendRegisterToFirebase(login: String, pass: String) {
    val email = ACTIVITY.getString(R.string.app_login_to_email, login)
    AUTH.createUserWithEmailAndPassword(email, pass)
            .addOnFailureListener { toastException(it) }
            .addOnSuccessListener {
                USER_ID = AUTH.currentUser?.uid.toString()
                USER.id = USER_ID
                USER.username = login
                USER.password = pass
                saveUserToDatabase()
            }
}

fun signOut(function: () -> Unit) {
    USER_ID = ""
    AUTH.signOut()
    function()
}