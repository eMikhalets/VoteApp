package com.emikhalets.voteapp.model.firebase

import android.net.Uri
import com.emikhalets.voteapp.utils.userId
import com.google.firebase.storage.StorageReference
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class FirebaseStorageRepository @Inject constructor(
        private val refStorage: StorageReference,
) {

    /**
     * Saves user image in storage and returns image url in callback.
     * Called [complete] when the server responds to a request.
     * If request is successful, callback returns a true, image name, url and an empty error message.
     * Else, callback returns false and a exception message
     * @param uri Image uri
     * @param complete Callback
     */
    fun saveImage(uri: Uri, complete: (success: Boolean, name: String, url: String, error: String) -> Unit) {
        Timber.d("Storage request: saveImage: STARTED")
        val imageName = UUID.randomUUID().toString()
        refStorage.child(FOLDER_IMAGES).child(imageName).putFile(uri)
                .addOnSuccessListener {
                    Timber.d("Storage request: saveImage: SUCCESS")
                    loadImageUrl(imageName) { isSuccess, url, error ->
                        if (isSuccess) complete(true, imageName, url, "")
                        else complete(false, "", "", error)
                    }
                }
                .addOnFailureListener {
                    Timber.d("Storage request: saveImage: FAILURE")
                    it.printStackTrace()
                    complete(false, "", "", it.message.toString())
                }
    }

    /**
     * Saves user profile photo in storage and returns photo url in callback.
     * Called [complete] when the server responds to a request.
     * If request is successful, callback returns a true, photo url and an empty error message.
     * Else, callback returns false, empty photo url and a exception message
     * @param uri Image uri
     * @param complete Callback
     */
    fun saveUserPhoto(uri: Uri, complete: (success: Boolean, url: String, error: String) -> Unit) {
        Timber.d("Storage request: saveUserPhoto: STARTED")
        refStorage.child(FOLDER_PROFILES).child(userId()).putFile(uri)
                .addOnSuccessListener {
                    Timber.d("Storage request: saveUserPhoto: SUCCESS")
                    loadUserPhotoUrl { isSuccess, url, error ->
                        if (isSuccess) complete(true, url, "")
                        else complete(false, "", error)
                    }
                }
                .addOnFailureListener {
                    Timber.d("Storage request: saveUserPhoto: FAILURE")
                    it.printStackTrace()
                    complete(false, "", it.message.toString())
                }
    }

    /**
     * Deleted image from cloud storage.
     * Called [complete] when the server responds to a request.
     * If request is successful, callback returns a true and an empty error message.
     * Else, callback returns false and a exception message
     * @param name Image name
     * @param complete Callback
     */
    fun deleteImage(name: String, complete: (success: Boolean, error: String) -> Unit) {
        Timber.d("Storage request: deleteImage: STARTED")
        refStorage.child(FOLDER_IMAGES).child(name).delete()
                .addOnSuccessListener {
                    Timber.d("Storage request: deleteImage: SUCCESS")
                    complete(true, "")
                }
                .addOnFailureListener {
                    Timber.d("Storage request: deleteImage: FAILURE")
                    it.printStackTrace()
                    complete(false, it.message.toString())
                }
    }

    /**
     * Uploads an image to cloud storage.
     * Called [complete] when the server responds to a request.
     * If request is successful, callback returns a true, image url and an empty error message.
     * Else, callback returns false, empty image url and a exception message
     * @param name Image name
     * @param complete Callback
     */
    private fun loadImageUrl(name: String, complete: (success: Boolean, url: String, error: String) -> Unit) {
        Timber.d("Storage request: loadImageUrl: STARTED")
        refStorage.child(FOLDER_IMAGES).child(name).downloadUrl
                .addOnSuccessListener {
                    Timber.d("Storage request: loadImageUrl: SUCCESS")
                    complete(true, it.toString(), "")
                }
                .addOnFailureListener {
                    Timber.d("Storage request: loadImageUrl: FAILURE")
                    it.printStackTrace()
                    complete(false, "", it.message.toString())
                }
    }

    /**
     * Uploads a profile photo to cloud storage.
     * Called [complete] when the server responds to a request.
     * If request is successful, callback returns a true, photo url and an empty error message.
     * Else, callback returns false, empty photo url and a exception message
     * @param complete Callback
     */
    private fun loadUserPhotoUrl(complete: (success: Boolean, url: String, error: String) -> Unit) {
        Timber.d("Storage request: loadUserPhotoUrl: STARTED")
        refStorage.child(FOLDER_PROFILES).child(userId()).downloadUrl
                .addOnSuccessListener {
                    Timber.d("Storage request: loadUserPhotoUrl: SUCCESS")
                    complete(true, it.toString(), "")
                }
                .addOnFailureListener {
                    Timber.d("Storage request: loadUserPhotoUrl: FAILURE")
                    it.printStackTrace()
                    complete(false, "", it.message.toString())
                }
    }

    private companion object {
        const val FOLDER_IMAGES = "images"
        const val FOLDER_PROFILES = "profile_images"
    }
}