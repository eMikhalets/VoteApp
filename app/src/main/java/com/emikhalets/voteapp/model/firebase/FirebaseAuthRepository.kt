package com.emikhalets.voteapp.model.firebase

import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.utils.*

class FirebaseAuthRepository {

    inline fun login(login: String, pass: String, crossinline onSuccess: () -> Unit) {
        val email = ACTIVITY.getString(R.string.app_login_to_email, login)
        AUTH.signInWithEmailAndPassword(email, pass)
                .addOnFailureListener { toastException(it) }
                .addOnSuccessListener {
                    USER_ID = AUTH.currentUser?.uid.toString()
                    onSuccess()
                }
    }

    inline fun register(login: String, pass: String, crossinline onSuccess: () -> Unit) {
        val email = ACTIVITY.getString(R.string.app_login_to_email, login)
        AUTH.createUserWithEmailAndPassword(email, pass)
                .addOnFailureListener { toastException(it) }
                .addOnSuccessListener {
                    USER_ID = AUTH.currentUser?.uid.toString()
                    onSuccess()
                }
    }

    inline fun logOut(crossinline onComplete: () -> Unit) {
        USER_ID = ""
        USER = User()
        AUTH.signOut()
        onComplete()
    }
}