package com.emikhalets.voteapp.model.firebase

import android.net.Uri
import com.emikhalets.voteapp.utils.USER_ID
import com.emikhalets.voteapp.utils.toastException
import com.google.firebase.storage.FirebaseStorage

class FirebaseStorageRepository {

    val refStorage = FirebaseStorage.getInstance().reference

    inline fun getImageUrl(name: String, crossinline onSuccess: (url: String) -> Unit) {
        REF_STORAGE.child(FOLDER_IMAGES).child(name).downloadUrl
                .addOnFailureListener { toastException(it) }
                .addOnSuccessListener { onSuccess(it.toString()) }
    }

    inline fun getProfileImageUrl(crossinline onSuccess: (url: String) -> Unit) {
        REF_STORAGE.child(FOLDER_PROFILES).child(USER_ID).downloadUrl
                .addOnFailureListener { toastException(it) }
                .addOnSuccessListener { onSuccess(it.toString()) }
    }

    inline fun saveProfileImage(uri: Uri, crossinline onSuccess: () -> Unit) {
        refStorage.child(FOLDER_PROFILES).child(USER_ID).putFile(uri)
                .addOnFailureListener { toastException(it) }
                .addOnSuccessListener { onSuccess() }
    }

    companion object {
        const val FOLDER_IMAGES = "images"
        const val FOLDER_PROFILES = "profile_images"
    }
}