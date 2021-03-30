package com.emikhalets.voteapp.model.firebase

import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.utils.USERNAME
import com.emikhalets.voteapp.utils.USER_ID
import com.emikhalets.voteapp.utils.toastException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class FirebaseAuthRepository {

    private val auth = Firebase.auth
    private var user = auth.currentUser

    init {
        USER_ID = user?.uid ?: ""
        USERNAME = user?.displayName ?: ""
    }

    fun login(login: String, pass: String, onSuccess: () -> Unit) {
        Timber.d("Authentication request: signInWithEmailAndPassword: STARTED")
        val email = ACTIVITY.getString(R.string.app_login_to_email, login)
        auth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener {
                    Timber.d("Authentication request: signInWithEmailAndPassword: SUCCESS")
                    user = auth.currentUser
                    USER_ID = user?.uid ?: ""
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
                    Timber.d("Authentication request: createUserWithEmailAndPassword: SUCCESS")
                    user = auth.currentUser
                    USER_ID = user?.uid ?: ""
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
        user = null
        USER_ID = ""
        auth.signOut()
        Timber.d("Authentication request: signOut: COMPLETE")
        onComplete()
    }

    fun updateUserPassword(pass: String, onSuccess: () -> Unit) {
        Timber.d("Authentication request: signInWithEmailAndPassword: STARTED")
        user?.updatePassword(pass)
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
        user?.updateProfile(profile)
                ?.addOnSuccessListener {
                    Timber.d("Authentication request: updateProfile: SUCCESS")
                    USERNAME = name
                    onSuccess(name)
                }
                ?.addOnFailureListener {
                    Timber.d("Authentication request: updateProfile: FAILURE")
                    toastException(it)
                }
    }
}