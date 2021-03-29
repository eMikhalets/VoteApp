package com.emikhalets.voteapp.model.firebase

import android.net.Uri
import com.emikhalets.voteapp.utils.USER_ID
import com.emikhalets.voteapp.utils.toastException
import com.google.firebase.storage.FirebaseStorage

const val FOLDER_IMAGES = "images"
const val FOLDER_PROFILES = "profile_images"

val REF_STORAGE = FirebaseStorage.getInstance().reference

inline fun saveImageToStorage(
        uri: Uri,
        crossinline function: (url: String, key: String) -> Unit
) {
    val key = getNewKeyFromImagesNode()
    REF_STORAGE.child(FOLDER_IMAGES).child(key).putFile(uri)
            .addOnFailureListener { toastException(it) }
            .addOnSuccessListener { getImageUrl(key) { url -> function(url, key) } }
}

inline fun getImageUrl(key: String, crossinline function: (url: String) -> Unit) {
    REF_STORAGE.child(FOLDER_IMAGES).child(key).downloadUrl
            .addOnFailureListener { toastException(it) }
            .addOnSuccessListener { function(it.toString()) }
}

inline fun saveProfileImageToStorage(uri: Uri, crossinline function: () -> Unit) {
    REF_STORAGE.child(FOLDER_PROFILES).child(USER_ID).putFile(uri)
            .addOnFailureListener { toastException(it) }
            .addOnSuccessListener { function() }
}

inline fun getProfileImageUrl(crossinline function: (url: String) -> Unit) {
    REF_STORAGE.child(FOLDER_PROFILES).child(USER_ID).downloadUrl
            .addOnFailureListener { toastException(it) }
            .addOnSuccessListener { function(it.toString()) }
}