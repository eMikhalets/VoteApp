package com.emikhalets.voteapp.model.firebase

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import timber.log.Timber
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
        private val auth: FirebaseAuth,
) {

    /**
     * Log in a user.
     * Called [complete] when the server responds to a request.
     * If request is successful, callback returns a true and an empty error message.
     * Else, callback returns false and a exception message
     * @param login User e-mail
     * @param pass User password
     * @param complete Callback
     */
    fun login(login: String, pass: String, complete: (success: Boolean, error: String) -> Unit) {
        Timber.d("Authentication request: signInWithEmailAndPassword: STARTED")
        auth.signInWithEmailAndPassword(login, pass)
                .addOnSuccessListener {
                    Timber.d("Authentication request: signInWithEmailAndPassword: SUCCESS")
                    complete(true, "")
                }
                .addOnFailureListener {
                    Timber.d("Authentication request: signInWithEmailAndPassword: FAILURE")
                    it.printStackTrace()
                    complete(false, it.message.toString())
                }
    }

    /**
     * Registers a new user and sets a username based by e-mail.
     * Called [complete] when the server responds to a request.
     * If request is successful, callback returns a true and an empty error message.
     * Else, callback returns false and a exception message
     * @param login User e-mail
     * @param pass User password
     * @param complete Callback
     */
    fun register(login: String, pass: String, complete: (success: Boolean, error: String) -> Unit) {
        Timber.d("Authentication request: createUserWithEmailAndPassword: STARTED")
        val username = login.split("@")[0]
        auth.createUserWithEmailAndPassword(login, pass)
                .addOnSuccessListener {
                    updateUsername(username) { isSuccess, error ->
                        Timber.d("Authentication request: createUserWithEmailAndPassword: SUCCESS")
                        if (isSuccess) complete(true, "")
                        else complete(false, error)
                    }
                }
                .addOnFailureListener {
                    Timber.d("Authentication request: createUserWithEmailAndPassword: FAILURE")
                    it.printStackTrace()
                    complete(false, it.message.toString())
                }
    }

    /**
     * Sing out and change current user (change authentication provider).
     * Called [complete] when the server responds to a request
     * @param complete Callback
     */
    fun logOut(complete: () -> Unit) {
        Timber.d("Authentication request: signOut: STARTED")
        auth.signOut()
        Timber.d("Authentication request: signOut: COMPLETE")
        complete()
    }

    /**
     * Updates the password of the current user.
     * Called [complete] when the server responds to a request.
     * If request is successful, callback returns a true and an empty error message.
     * Else, callback returns false and a exception message
     * @param pass New password of the current user
     * @param complete Callback
     */
    fun updateUserPassword(pass: String, complete: (success: Boolean, error: String) -> Unit) {
        Timber.d("Authentication request: signInWithEmailAndPassword: STARTED")
        auth.currentUser?.updatePassword(pass)
                ?.addOnSuccessListener {
                    Timber.d("Authentication request: signInWithEmailAndPassword: SUCCESS")
                    complete(true, "")
                }
                ?.addOnFailureListener {
                    Timber.d("Authentication request: signInWithEmailAndPassword: FAILURE")
                    it.printStackTrace()
                    complete(false, it.message.toString())
                }
    }

    /**
     * Updates the username of the current user.
     * Called [complete] when the server responds to a request.
     * If request is successful, callback returns a true and an empty error message.
     * Else, callback returns false and a exception message
     * @param name New name of the current user
     * @param complete Callback
     */
    fun updateUsername(name: String, complete: (success: Boolean, error: String) -> Unit) {
        Timber.d("Authentication request: updateUsername: STARTED")
        val profile = UserProfileChangeRequest.Builder().setDisplayName(name).build()
        auth.currentUser?.updateProfile(profile)
                ?.addOnSuccessListener {
                    Timber.d("Authentication request: updateUsername: SUCCESS")
                    complete(true, "")
                }
                ?.addOnFailureListener {
                    Timber.d("Authentication request: updateUsername: FAILURE")
                    it.printStackTrace()
                    complete(false, it.message.toString())
                }
    }

    /**
     * Updates the profile photo of the current user.
     * Called [complete] when the server responds to a request.
     * If request is successful, callback returns a true and an empty error message.
     * Else, callback returns false and a exception message
     * @param url Url of the profile photo in Firebase Cloud Storage
     * @param complete Callback
     */
    fun updateUserPhoto(url: String, complete: (success: Boolean, error: String) -> Unit) {
        Timber.d("Authentication request: updateUserPhoto: STARTED")
        val profile = UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(url)).build()
        auth.currentUser?.updateProfile(profile)
                ?.addOnSuccessListener {
                    Timber.d("Authentication request: updateUserPhoto: SUCCESS")
                    complete(true, "")
                }
                ?.addOnFailureListener {
                    Timber.d("Authentication request: updateUserPhoto: FAILURE")
                    it.printStackTrace()
                    complete(false, it.message.toString())
                }
    }
}