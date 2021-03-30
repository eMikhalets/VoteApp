package com.emikhalets.voteapp.model.firebase

import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.utils.USERNAME
import com.emikhalets.voteapp.utils.USER_ID
import com.emikhalets.voteapp.utils.singleDataChange
import com.emikhalets.voteapp.utils.toastException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import timber.log.Timber

class FirebaseDatabaseRepository {

    private val refDatabase = FirebaseDatabase.getInstance().reference

    private fun DataSnapshot.toUser(): User = this.getValue(User::class.java) ?: User()

    private fun DataSnapshot.toImage(): Image = this.getValue(Image::class.java) ?: Image()

    fun checkUserExisting(onSuccess: (Boolean) -> Unit) {
        Timber.d("Database request: checkUserExisting: STARTED")
        refDatabase.child(NODE_USERS).child(USER_ID).singleDataChange {
            Timber.d("Database request: checkUserExisting: COMPLETE")
            onSuccess(it.exists())
        }
    }

    fun loadUserData(onSuccess: (User) -> Unit) {
        Timber.d("Database request: loadUserData: STARTED")
        refDatabase.child(NODE_USERS).child(USER_ID).singleDataChange { snapshot ->
            if (snapshot.exists()) {
                Timber.d("Database request: loadUserData: SUCCESS")
                onSuccess(snapshot.toUser())
            } else {
                Timber.d("Database request: loadUserData: FAILURE")
            }
        }
    }

    fun loadUserImages(onComplete: (List<Image>) -> Unit) {
        Timber.d("Database request: loadUserImages: STARTED")
        refDatabase.child(NODE_IMAGES).child(USER_ID).singleDataChange { snapshot ->
            Timber.d("Database request: loadUserImages: COMPLETE")
            val list = mutableListOf<Image>()
            snapshot.children.forEach { list.add(it.toImage()) }
            onComplete(list)
        }
    }

    fun loadAddedUserImage(name: String, onComplete: (Image) -> Unit) {
        Timber.d("Database request: loadAddedUserImage: STARTED")
        refDatabase.child(NODE_IMAGES).child(USER_ID).child(name).singleDataChange { snapshot ->
            Timber.d("Database request: loadAddedUserImage: COMPLETE")
            onComplete(snapshot.toImage())
        }
    }

    fun saveUser(login: String, onSuccess: () -> Unit) {
        Timber.d("Database request: saveUser: STARTED")
        val map = hashMapOf(
                CHILD_ID to USER_ID,
                CHILD_USERNAME to login,
                CHILD_PHOTO to "",
                CHILD_RATING to 0
        )
        refDatabase.child(NODE_USERS).child(USER_ID).setValue(map)
                .addOnSuccessListener {
                    Timber.d("Database request: saveUser: SUCCESS")
                    onSuccess()
                }
                .addOnFailureListener {
                    Timber.d("Database request: saveUser: FAILURE")
                    toastException(it)
                }
    }

    fun saveImage(name: String, url: String, onSuccess: () -> Unit) {
        Timber.d("Database request: saveImage: STARTED")
        val map = hashMapOf(
                CHILD_NAME to name,
                CHILD_URL to url,
                CHILD_RATING to 0,
                CHILD_OWNER_ID to USER_ID,
                CHILD_OWNER_NAME to USERNAME,
                CHILD_TIMESTAMP to ServerValue.TIMESTAMP
        )
        refDatabase.child(NODE_IMAGES).child(USER_ID).child(name).setValue(map)
                .addOnSuccessListener {
                    Timber.d("Database request: saveImage: SUCCESS")
                    onSuccess()
                }
                .addOnFailureListener {
                    Timber.d("Database request: saveImage: FAILURE")
                    toastException(it)
                }
    }

    fun updateUserPhoto(url: String, onSuccess: () -> Unit) {
        Timber.d("Database request: updateUserPhoto: STARTED")
        val map = hashMapOf<String, Any>(CHILD_PHOTO to url)
        refDatabase.child(NODE_USERS).child(USER_ID).updateChildren(map)
                .addOnSuccessListener {
                    Timber.d("Database request: updateUserPhoto: SUCCESS")
                    onSuccess()
                }
                .addOnFailureListener {
                    Timber.d("Database request: updateUserPhoto: FAILURE")
                    toastException(it)
                }
    }

    companion object {
        private const val NODE_IMAGES = "images"
        private const val NODE_USERS = "users"

        private const val CHILD_OWNER_NAME = "owner_name"
        private const val CHILD_TIMESTAMP = "timestamp"
        private const val CHILD_OWNER_ID = "owner_id"
        private const val CHILD_USERNAME = "username"
        private const val CHILD_RATING = "rating"
        private const val CHILD_PHOTO = "photo"
        private const val CHILD_NAME = "name"
        private const val CHILD_URL = "url"
        private const val CHILD_ID = "id"
    }
}