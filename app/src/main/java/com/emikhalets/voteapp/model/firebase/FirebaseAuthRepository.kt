package com.emikhalets.voteapp.model.firebase

import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.utils.USER
import com.emikhalets.voteapp.utils.toastException
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
        private val auth: FirebaseAuth,
) {

    fun login(login: String, pass: String, complete: (Boolean, String) -> Unit) {
        Timber.d("Authentication request: signInWithEmailAndPassword: STARTED")
        auth.signInWithEmailAndPassword(login, pass)
                .addOnSuccessListener {
                    USER = User(id = auth.currentUser?.uid ?: "")
                    Timber.d("Authentication request: signInWithEmailAndPassword: SUCCESS")
                    complete(true, "")
                }
                .addOnFailureListener {
                    Timber.d("Authentication request: signInWithEmailAndPassword: FAILURE")
                    it.printStackTrace()
                    complete(false, it.message.toString())
                }
    }

    fun register(login: String, pass: String, complete: (Boolean, String) -> Unit) {
        Timber.d("Authentication request: createUserWithEmailAndPassword: STARTED")
        val username = login.split("@")[0]
        auth.createUserWithEmailAndPassword(login, pass)
                .addOnSuccessListener {
                    USER = User(
                            id = auth.currentUser?.uid ?: "",
                            username = username,
                            login = login,
                            photo = "null"
                    )
                    Timber.d("Authentication request: createUserWithEmailAndPassword: SUCCESS")
                    complete(true, "")
                }
                .addOnFailureListener {
                    Timber.d("Authentication request: createUserWithEmailAndPassword: FAILURE")
                    it.printStackTrace()
                    complete(false, it.message.toString())
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