package com.emikhalets.voteapp.model.firebase

import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.utils.*

class FirebaseAuthRepository {

    inline fun login(login: String, pass: String, crossinline onSuccess: () -> Unit) {
        val email = ACTIVITY.getString(R.string.app_login_to_email, login)
        AUTH.signInWithEmailAndPassword(email, pass)
                .addOnFailureListener { toastException(it) }
                .addOnSuccessListener {
                    USER = AUTH.currentUser
                    USER_ID = USER?.uid ?: ""
                    onSuccess()
                }
    }

    inline fun register(login: String, pass: String, crossinline onSuccess: () -> Unit) {
        val email = ACTIVITY.getString(R.string.app_login_to_email, login)
        AUTH.createUserWithEmailAndPassword(email, pass)
                .addOnFailureListener { toastException(it) }
                .addOnSuccessListener {
                    USER = AUTH.currentUser
                    USER_ID = USER?.uid ?: ""
                    onSuccess()
                }
    }

    inline fun logOut(crossinline onComplete: () -> Unit) {
        USER = null
        USER_ID = ""
        AUTH.signOut()
        onComplete()
    }

    inline fun updateUserPassword(pass: String, crossinline onSuccess: () -> Unit) {
        USER?.updatePassword(pass)
                ?.addOnFailureListener { toastException(it) }
                ?.addOnSuccessListener { onSuccess() }
    }
}