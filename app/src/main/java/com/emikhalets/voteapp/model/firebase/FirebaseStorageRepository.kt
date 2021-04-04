package com.emikhalets.voteapp.model.firebase

import android.net.Uri
import com.emikhalets.voteapp.utils.USER
import com.emikhalets.voteapp.utils.toastException
import com.google.firebase.storage.StorageReference
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class FirebaseStorageRepository @Inject constructor(
        private val refStorage: StorageReference,
) {

    fun saveImage(uri: Uri, onSuccess: (String, String) -> Unit) {
        Timber.d("Storage request: saveImage: STARTED")
        val imageName = UUID.randomUUID().toString()
        refStorage.child(FOLDER_IMAGES).child(imageName).putFile(uri)
                .addOnSuccessListener {
                    Timber.d("Storage request: saveImage: SUCCESS")
                    loadImageUrl(imageName) { url ->
                        onSuccess(imageName, url)
                    }
                }
                .addOnFailureListener {
                    Timber.d("Storage request: saveImage: FAILURE")
                    toastException(it)
                }
    }

    fun saveUserPhoto(uri: Uri, onSuccess: (String) -> Unit) {
        Timber.d("Storage request: saveUserPhoto: STARTED")
        refStorage.child(FOLDER_PROFILES).child(USER.id).putFile(uri)
                .addOnSuccessListener {
                    Timber.d("Storage request: saveUserPhoto: SUCCESS")
                    loadUserPhotoUrl { url ->
                        onSuccess(url)
                    }
                }
                .addOnFailureListener {
                    Timber.d("Storage request: saveUserPhoto: FAILURE")
                    toastException(it)
                }
    }

    fun deleteImage(name: String, onSuccess: () -> Unit) {
        Timber.d("Storage request: deleteImage: STARTED")
        refStorage.child(FOLDER_IMAGES).child(name).delete()
                .addOnSuccessListener {
                    Timber.d("Storage request: deleteImage: SUCCESS")
                    onSuccess()
                }
                .addOnFailureListener {
                    Timber.d("Storage request: deleteImage: FAILURE")
                    toastException(it)
                }
    }

    private fun loadImageUrl(name: String, onSuccess: (url: String) -> Unit) {
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

    private fun loadUserPhotoUrl(onSuccess: (url: String) -> Unit) {
        Timber.d("Storage request: loadUserPhotoUrl: STARTED")
        refStorage.child(FOLDER_PROFILES).child(USER.id).downloadUrl
                .addOnSuccessListener {
                    Timber.d("Storage request: loadUserPhotoUrl: SUCCESS")
                    onSuccess(it.toString())
                }
                .addOnFailureListener {
                    Timber.d("Storage request: loadUserPhotoUrl: FAILURE")
                    toastException(it)
                }
    }

    companion object {
        private const val FOLDER_IMAGES = "images"
        private const val FOLDER_PROFILES = "profile_images"
    }
}