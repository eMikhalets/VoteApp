package com.emikhalets.voteapp.model.firebase

import android.net.Uri
import com.emikhalets.voteapp.utils.AppResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
        private val auth: FirebaseAuth,
) {

    /**
     * Checks if the user is equal to zero or if his id is empty.
     * If check passed returns true, else returns error message in callback.
     * @param complete Callback
     */
    suspend fun checkAuth(complete: (AppResult<Boolean>) -> Unit) = withContext(Dispatchers.IO) {
        Timber.d("Authentication request: checkAuth: STARTED")
        if (auth.currentUser == null || auth.currentUser?.uid.toString().isEmpty()) {
            Timber.d("Authentication request: checkAuth: FAILURE")
            complete(AppResult.Error("You need to sign in"))
        } else {
            Timber.d("Authentication request: checkAuth: SUCCESS")
            complete(AppResult.Success(true))
        }
    }

    /**
     * SignIn a user.
     * If request is successful, callback returns true, else returns exception message
     * @param login User e-mail
     * @param pass User password
     * @param complete Callback
     */
    suspend fun login(login: String, pass: String, complete: (AppResult<Boolean>) -> Unit) = withContext(Dispatchers.IO) {
        Timber.d("Authentication request: signInWithEmailAndPassword: STARTED")
        auth.signInWithEmailAndPassword(login, pass)
                .addOnSuccessListener {
                    Timber.d("Authentication request: signInWithEmailAndPassword: SUCCESS")
                    complete(AppResult.Success(true))
                }
                .addOnFailureListener {
                    Timber.d("Authentication request: signInWithEmailAndPassword: FAILURE")
                    it.printStackTrace()
                    complete(AppResult.Error(it.message.toString()))
                }
    }

    /**
     * Registers a new user and sets a username based by e-mail, then updates displayed name of auth user
     * If request is successful, callback returns true, else returns exception message
     * @param login User e-mail
     * @param pass User password
     * @param complete Callback
     */
    suspend fun register(login: String, pass: String, complete: (AppResult<Boolean>) -> Unit) = withContext(Dispatchers.IO) {
        Timber.d("Authentication request: createUserWithEmailAndPassword: STARTED")
        auth.createUserWithEmailAndPassword(login, pass)
                .addOnSuccessListener {
                    Timber.d("Authentication request: createUserWithEmailAndPassword: SUCCESS")
                    val username = login.split("@")[0]
                    val profile = UserProfileChangeRequest.Builder().setDisplayName(username).build()
                    auth.currentUser?.updateProfile(profile)
                            ?.addOnSuccessListener {
                                Timber.d("Authentication request: updateProfile: SUCCESS")
                                complete(AppResult.Success(true))
                            }
                            ?.addOnFailureListener {
                                Timber.d("Authentication request: updateProfile: FAILURE")
                                it.printStackTrace()
                                complete(AppResult.Error(it.message.toString()))
                            }
                }
                .addOnFailureListener {
                    Timber.d("Authentication request: createUserWithEmailAndPassword: FAILURE")
                    it.printStackTrace()
                    complete(AppResult.Error(it.message.toString()))
                }
    }

    /**
     * Sing out and change current user (change authentication provider).
     * Called [complete] when the server responds to a request
     * @param complete Callback
     */
    suspend fun logOut(complete: () -> Unit) = withContext(Dispatchers.IO) {
        Timber.d("Authentication request: signOut: STARTED")
        auth.signOut()
        Timber.d("Authentication request: signOut: COMPLETE")
        complete()
    }

    /**
     * Updates the password of the current user.
     * If request is successful, callback returns a true, else returns exception message
     * @param pass New password of the current user
     * @param complete Callback
     */
    suspend fun updateUserPassword(pass: String, complete: (AppResult<Boolean>) -> Unit) = withContext(Dispatchers.IO) {
        Timber.d("Authentication request: signInWithEmailAndPassword: STARTED")
        auth.currentUser?.updatePassword(pass)
                ?.addOnSuccessListener {
                    Timber.d("Authentication request: signInWithEmailAndPassword: SUCCESS")
                    complete(AppResult.Success(true))
                }
                ?.addOnFailureListener {
                    Timber.d("Authentication request: signInWithEmailAndPassword: FAILURE")
                    it.printStackTrace()
                    complete(AppResult.Error(it.message.toString()))
                }
    }

    /**
     * Updates the username of the current user.
     * If request is successful, callback returns true, else returns exception message
     * @param name New name of the current user
     * @param complete Callback
     */
    suspend fun updateUsername(name: String, complete: (AppResult<Boolean>) -> Unit) = withContext(Dispatchers.IO) {
        Timber.d("Authentication request: updateUsername: STARTED")
        val profile = UserProfileChangeRequest.Builder().setDisplayName(name).build()
        auth.currentUser?.updateProfile(profile)
                ?.addOnSuccessListener {
                    Timber.d("Authentication request: updateUsername: SUCCESS")
                    complete(AppResult.Success(true))
                }
                ?.addOnFailureListener {
                    Timber.d("Authentication request: updateUsername: FAILURE")
                    it.printStackTrace()
                    complete(AppResult.Error(it.message.toString()))
                }
    }

    /**
     * Updates the profile photo of the current user.
     * If request is successful, callback returns true, else returns exception message
     * @param url Url of the profile photo in Firebase Cloud Storage
     * @param complete Callback
     */
    suspend fun updateUserPhoto(url: String, complete: (AppResult<Boolean>) -> Unit) = withContext(Dispatchers.IO) {
        Timber.d("Authentication request: updateUserPhoto: STARTED")
        val profile = UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(url)).build()
        auth.currentUser?.updateProfile(profile)
                ?.addOnSuccessListener {
                    Timber.d("Authentication request: updateUserPhoto: SUCCESS")
                    complete(AppResult.Success(true))
                }
                ?.addOnFailureListener {
                    Timber.d("Authentication request: updateUserPhoto: FAILURE")
                    it.printStackTrace()
                    complete(AppResult.Error(it.message.toString()))
                }
    }
}