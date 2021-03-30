package com.emikhalets.voteapp.model.firebase

import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.utils.*
import com.google.firebase.auth.UserProfileChangeRequest
import timber.log.Timber

class FirebaseAuthRepository {

    fun login(login: String, pass: String, onSuccess: () -> Unit) {
        Timber.d("Authentication request: signInWithEmailAndPassword: STARTED")
        val email = ACTIVITY.getString(R.string.app_login_to_email, login)
        AUTH.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener {
                    Timber.d("Authentication request: signInWithEmailAndPassword: SUCCESS")
                    USER = AUTH.currentUser
                    USER_ID = USER?.uid ?: ""
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
        AUTH.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener {
                    Timber.d("Authentication request: createUserWithEmailAndPassword: SUCCESS")
                    USER = AUTH.currentUser
                    USER_ID = USER?.uid ?: ""
                    updateUsername(login) {
                        onSuccess()
                    }
                }
                .addOnFailureListener {
                    Timber.d("Authentication request: createUserWithEmailAndPassword: FAILURE")
                    toastException(it)
                }
    }

    fun logOut(onComplete: () -> Unit) {
        Timber.d("Authentication request: signOut: STARTED")
        USER = null
        USER_ID = ""
        AUTH.signOut()
        Timber.d("Authentication request: signOut: COMPLETE")
        onComplete()
    }

    fun updateUserPassword(pass: String, onSuccess: () -> Unit) {
        Timber.d("Authentication request: signInWithEmailAndPassword: STARTED")
        USER?.updatePassword(pass)
                ?.addOnSuccessListener {
                    Timber.d("Authentication request: signInWithEmailAndPassword: SUCCESS")
                    onSuccess()
                }
                ?.addOnFailureListener {
                    Timber.d("Authentication request: signInWithEmailAndPassword: FAILURE")
                    toastException(it)
                }
    }

    fun updateUsername(name: String, onSuccess: (String) -> Unit) {
        Timber.d("Authentication request: updateProfile: STARTED")
        val profile = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
        USER?.updateProfile(profile)
                ?.addOnSuccessListener {
                    Timber.d("Authentication request: updateProfile: SUCCESS")
                    onSuccess(name)
                }
                ?.addOnFailureListener {
                    Timber.d("Authentication request: updateProfile: FAILURE")
                    toastException(it)
                }
    }
}