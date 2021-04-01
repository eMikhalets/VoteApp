package com.emikhalets.voteapp.model.firebase

import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.utils.USER
import com.emikhalets.voteapp.utils.toastException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class FirebaseAuthRepository {

    private val auth = Firebase.auth

    init {
        USER.id = auth.currentUser?.uid ?: ""
    }

    fun login(login: String, pass: String, onSuccess: () -> Unit) {
        Timber.d("Authentication request: signInWithEmailAndPassword: STARTED")
        val email = ACTIVITY.getString(R.string.app_login_to_email, login)
        auth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener {
                    USER = User(id = auth.currentUser?.uid ?: "")
                    Timber.d("Authentication request: signInWithEmailAndPassword: SUCCESS")
                    onSuccess()
                }
                .addOnFailureListener {
                    Timber.d("Authentication request: signInWithEmailAndPassword: FAILURE")
                    toastException(it)
                }
    }

    fun register(login: String, pass: String, onSuccess: () -> Unit) {
        Timber.d("Authentication request: createUserWithEmailAndPassword: STARTED")
        val email = ACTIVITY.getString(R.string.app_login_to_email, login)
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener {
                    USER = User(
                            id = auth.currentUser?.uid ?: "",
                            username = login,
                            login = login,
                            photo = "null"
                    )
                    Timber.d("Authentication request: createUserWithEmailAndPassword: SUCCESS")
                    onSuccess()
                }
                .addOnFailureListener {
                    Timber.d("Authentication request: createUserWithEmailAndPassword: FAILURE")
                    toastException(it)
                }
    }

    fun logOut(onComplete: () -> Unit) {
        Timber.d("Authentication request: signOut: STARTED")
        USER = User()
        auth.signOut()
        Timber.d("Authentication request: signOut: COMPLETE")
        onComplete()
    }

    fun updateUserPassword(pass: String, onSuccess: () -> Unit) {
        Timber.d("Authentication request: signInWithEmailAndPassword: STARTED")
        auth.currentUser?.updatePassword(pass)
                ?.addOnSuccessListener {
                    Timber.d("Authentication request: signInWithEmailAndPassword: SUCCESS")
                    onSuccess()
                }
                ?.addOnFailureListener {
                    Timber.d("Authentication request: signInWithEmailAndPassword: FAILURE")
                    toastException(it)
                }
    }
}