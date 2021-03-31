package com.emikhalets.voteapp.model.firebase

import android.net.Uri
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.utils.*
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
        USER_PHOTO = user?.photoUrl.toString()
    }

    fun login(login: String, pass: String, onSuccess: () -> Unit) {
        Timber.d("Authentication request: signInWithEmailAndPassword: STARTED")
        val email = ACTIVITY.getString(R.string.app_login_to_email, login)
        auth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener {
                    Timber.d("Authentication request: signInWithEmailAndPassword: SUCCESS")
                    user = auth.currentUser
                    USER_ID = user?.uid ?: ""
                    USERNAME = user?.displayName ?: ""
                    USER_PHOTO = user?.photoUrl.toString()
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
                    USER_PHOTO = ""
                    onSuccess()
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

    fun updateUsername(name: String, onSuccess: () -> Unit) {
        Timber.d("Authentication request: updateProfile: STARTED")
        val profile = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
        user?.updateProfile(profile)
                ?.addOnSuccessListener {
                    Timber.d("Authentication request: updateProfile: SUCCESS")
                    USERNAME = name
                    onSuccess()
                }
                ?.addOnFailureListener {
                    Timber.d("Authentication request: updateProfile: FAILURE")
                    toastException(it)
                }
    }

    fun updateUserPhoto(url: String, onSuccess: () -> Unit) {
        Timber.d("Authentication request: updateProfile: STARTED")
        val uri = Uri.parse(url)
        val profile = UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build()
        user?.updateProfile(profile)
                ?.addOnSuccessListener {
                    Timber.d("Authentication request: updateProfile: SUCCESS")
                    USER_PHOTO = uri.toString()
                    onSuccess()
                }
                ?.addOnFailureListener {
                    Timber.d("Authentication request: updateProfile: FAILURE")
                    toastException(it)
                }
    }
}