package com.emikhalets.voteapp.model.firebase

import android.net.Uri
import com.emikhalets.voteapp.utils.AppResult
import com.emikhalets.voteapp.utils.userId
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class FirebaseStorageRepository @Inject constructor(
        private val refStorage: StorageReference,
) {

    /**
     * Saves user image in storage and returns image url in callback.
     * If request is successful, callback returns image url, else returns exception message
     * @param uri Image uri
     * @param complete Callback
     */
    suspend fun saveImage(name: String, uri: Uri, complete: (AppResult<String>) -> Unit) = withContext(Dispatchers.IO) {
        Timber.d("Storage request: saveImage: STARTED")
        refStorage.child(FOLDER_IMAGES).child(name).putFile(uri)
                .addOnSuccessListener {
                    Timber.d("Storage request: saveImage: SUCCESS")
                    refStorage.child(FOLDER_IMAGES).child(name).downloadUrl
                            .addOnSuccessListener {
                                Timber.d("Storage request: downloadImageUrl: SUCCESS")
                                complete(AppResult.Success(it.toString()))
                            }
                            .addOnFailureListener {
                                Timber.d("Storage request: downloadImageUrl: FAILURE")
                                it.printStackTrace()
                                complete(AppResult.Error(it.message.toString()))
                            }
                }
                .addOnFailureListener {
                    Timber.d("Storage request: saveImage: FAILURE")
                    it.printStackTrace()
                    complete(AppResult.Error(it.message.toString()))
                }
    }

    /**
     * Saves user profile photo in storage and returns photo url in callback.
     * If request is successful, callback returns photo url, else returns exception message
     * @param uri Image uri
     * @param complete Callback
     */
    suspend fun saveUserPhoto(uri: Uri, complete: (AppResult<String>) -> Unit) = withContext(Dispatchers.IO) {
        Timber.d("Storage request: saveUserPhoto: STARTED")
        refStorage.child(FOLDER_PROFILES).child(userId()).putFile(uri)
                .addOnSuccessListener {
                    Timber.d("Storage request: saveUserPhoto: SUCCESS")
                    refStorage.child(FOLDER_PROFILES).child(userId()).downloadUrl
                            .addOnSuccessListener {
                                Timber.d("Storage request: downloadUserPhotoUrl: SUCCESS")
                                complete(AppResult.Success(it.toString()))
                            }
                            .addOnFailureListener {
                                Timber.d("Storage request: downloadUserPhotoUrl: FAILURE")
                                it.printStackTrace()
                                complete(AppResult.Error(it.message.toString()))
                            }
                }
                .addOnFailureListener {
                    Timber.d("Storage request: saveUserPhoto: FAILURE")
                    it.printStackTrace()
                    complete(AppResult.Error(it.message.toString()))
                }
    }

    /**
     * Deleted image from cloud storage.
     * If request is successful, callback returns true, else returns exception message
     * @param name Image name
     * @param complete Callback
     */
    suspend fun deleteImage(name: String, complete: (AppResult<Boolean>) -> Unit) = withContext(Dispatchers.IO) {
        Timber.d("Storage request: deleteImage: STARTED")
        refStorage.child(FOLDER_IMAGES).child(name).delete()
                .addOnSuccessListener {
                    Timber.d("Storage request: deleteImage: SUCCESS")
                    complete(AppResult.Success(true))
                }
                .addOnFailureListener {
                    Timber.d("Storage request: deleteImage: FAILURE")
                    it.printStackTrace()
                    complete(AppResult.Error(it.message.toString()))
                }
    }

    private companion object {
        const val FOLDER_IMAGES = "images"
        const val FOLDER_PROFILES = "profile_images"
    }
}