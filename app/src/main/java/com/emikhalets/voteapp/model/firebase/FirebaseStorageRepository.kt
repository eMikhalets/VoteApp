package com.emikhalets.voteapp.model.firebase

import android.net.Uri
import com.emikhalets.voteapp.utils.USER_ID
import com.emikhalets.voteapp.utils.toastException
import com.google.firebase.storage.FirebaseStorage
import timber.log.Timber
import java.util.*

class FirebaseStorageRepository {

    private val refStorage = FirebaseStorage.getInstance().reference

    fun loadImageUrl(name: String, onSuccess: (url: String) -> Unit) {
        Timber.d("Storage request: loadImageUrl: STARTED")
        refStorage.child(FOLDER_IMAGES).child(name).downloadUrl
                .addOnSuccessListener {
                    Timber.d("Storage request: loadImageUrl: SUCCESS")
                    onSuccess(it.toString())
                }
                .addOnFailureListener {
                    Timber.d("Storage request: loadImageUrl: FAILURE")
                    toastException(it)
                }
    }

    fun loadUserPhotoUrl(onSuccess: (url: String) -> Unit) {
        Timber.d("Storage request: loadUserPhotoUrl: STARTED")
        refStorage.child(FOLDER_PROFILES).child(USER_ID).downloadUrl
                .addOnSuccessListener {
                    Timber.d("Storage request: loadUserPhotoUrl: SUCCESS")
                    onSuccess(it.toString())
                }
                .addOnFailureListener {
                    Timber.d("Storage request: loadUserPhotoUrl: FAILURE")
                    toastException(it)
                }
    }

    fun saveImage(uri: Uri, onSuccess: (String) -> Unit) {
        Timber.d("Storage request: saveImage: STARTED")
        val imageName = UUID.randomUUID().toString()
        refStorage.child(FOLDER_IMAGES).child(imageName).putFile(uri)
                .addOnSuccessListener {
                    Timber.d("Storage request: saveImage: SUCCESS")
                    onSuccess(imageName)
                }
                .addOnFailureListener {
                    Timber.d("Storage request: saveImage: FAILURE")
                    toastException(it)
                }
    }

    fun saveUserPhoto(uri: Uri, onSuccess: () -> Unit) {
        Timber.d("Storage request: saveUserPhoto: STARTED")
        refStorage.child(FOLDER_PROFILES).child(USER_ID).putFile(uri)
                .addOnSuccessListener {
                    Timber.d("Storage request: saveUserPhoto: SUCCESS")
                    onSuccess()
                }
                .addOnFailureListener {
                    Timber.d("Storage request: saveUserPhoto: FAILURE")
                    toastException(it)
                }
    }

    companion object {
        private const val FOLDER_IMAGES = "images"
        private const val FOLDER_PROFILES = "profile_images"
    }
}