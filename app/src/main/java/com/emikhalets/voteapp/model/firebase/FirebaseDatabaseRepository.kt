package com.emikhalets.voteapp.model.firebase

import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.utils.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ServerValue
import timber.log.Timber
import javax.inject.Inject

class FirebaseDatabaseRepository @Inject constructor(
        private val refDatabase: DatabaseReference,
) {

    fun loadUserData(complete: (User?, String) -> Unit) {
        Timber.d("Database request: loadUserData: STARTED")
        refDatabase.child(NODE_USERS).child(USER.id).singleDataChange { snapshot ->
            if (snapshot.exists()) {
                Timber.d("Database request: loadUserData: COMPLETE (User exist)")
                USER = snapshot.toUser()
                complete(snapshot.toUser(), "")
            } else {
                Timber.d("Database request: loadUserData: COMPLETE (User not exist)")
                complete(null, "User not exist in database")
            }
        }
    }

    fun loadUserImages(onComplete: (List<Image>) -> Unit) {
        Timber.d("Database request: loadUserImages: STARTED")
        refDatabase.child(NODE_IMAGES).orderByChild(CHILD_OWNER_ID).equalTo(USER.id)
                .singleDataChange { snapshot ->
                    val list = mutableListOf<Image>()
                    snapshot.children.forEach { list.add(it.toImage()) }
                    Timber.d("Database request: loadUserImages: COMPLETE")
                    onComplete(list)
                }
    }

    fun loadLatestImages(onComplete: (List<Image>) -> Unit) {
        Timber.d("Database request: loadLatestImages: STARTED")
        refDatabase.child(NODE_IMAGES).orderByChild(CHILD_TIMESTAMP).limitToLast(30)
                .singleDataChange { snapshot ->
                    val list = mutableListOf<Image>()
                    snapshot.children.forEach { list.add(it.toImage()) }
                    Timber.d("Database request: loadLatestImages: COMPLETE")
                    onComplete(list)
                }
    }

    fun loadTopImages(onComplete: (List<Image>) -> Unit) {
        Timber.d("Database request: loadTopImages: STARTED")
        refDatabase.child(NODE_IMAGES).orderByChild(CHILD_RATING).singleDataChange { snapshot ->
            val list = mutableListOf<Image>()
            snapshot.children.forEach { list.add(it.toImage()) }
            Timber.d("Database request: loadTopImages: COMPLETE")
            onComplete(list)
        }
    }

    fun loadTopUsers(onComplete: (List<User>) -> Unit) {
        Timber.d("Database request: loadTopUsers: STARTED")
        refDatabase.child(NODE_USERS).orderByChild(CHILD_RATING).singleDataChange { snapshot ->
            val list = mutableListOf<User>()
            snapshot.children.forEach { list.add(it.toUser()) }
            Timber.d("Database request: loadTopUsers: COMPLETE")
            onComplete(list)
        }
    }

    fun loadAllImages(onComplete: (List<Image>) -> Unit) {
        Timber.d("Database request: loadAllImages: STARTED")
        refDatabase.child(NODE_IMAGES).singleDataChange { snapshot ->
            val list = mutableListOf<Image>()
            snapshot.children.forEach { list.add(it.toImage()) }
            Timber.d("Database request: loadAllImages: COMPLETE")
            onComplete(list)
        }
    }

    fun saveUser(complete: (Boolean, String) -> Unit) {
        Timber.d("Database request: saveUser: STARTED")
        val map = hashMapOf(
                CHILD_ID to USER.id,
                CHILD_LOGIN to USER.login,
                CHILD_USERNAME to USER.username,
                CHILD_PHOTO to "",
                CHILD_RATING to 0
        )
        refDatabase.child(NODE_USERS).child(USER.id).setValue(map)
                .addOnSuccessListener {
                    Timber.d("Database request: saveUser: SUCCESS")
                    complete(true, "")
                }
                .addOnFailureListener {
                    Timber.d("Database request: saveUser: FAILURE")
                    it.printStackTrace()
                    complete(false, it.message.toString())
                }
    }

    fun saveUserImage(name: String, url: String, onSuccess: (Image) -> Unit) {
        Timber.d("Database request: saveImage: STARTED")
        val map = hashMapOf(
                CHILD_NAME to name,
                CHILD_URL to url,
                CHILD_RATING to 0,
                CHILD_OWNER_ID to USER.id,
                CHILD_OWNER_NAME to USER.username,
                CHILD_TIMESTAMP to ServerValue.TIMESTAMP
        )
        refDatabase.child(NODE_IMAGES).child(name).setValue(map)
                .addOnSuccessListener {
                    Timber.d("Database request: saveImage: SUCCESS")
                    loadAddedUserImage(name) { image ->
                        onSuccess(image)
                    }
                }
                .addOnFailureListener {
                    Timber.d("Database request: saveImage: FAILURE")
                    toastException(it)
                }
    }

    fun updateUserPhoto(url: String, onSuccess: () -> Unit) {
        Timber.d("Database request: updateUserPhoto: STARTED")
        val map = hashMapOf<String, Any>(CHILD_PHOTO to url)
        refDatabase.child(NODE_USERS).child(USER.id).updateChildren(map)
                .addOnSuccessListener {
                    USER.photo = url
                    Timber.d("Database request: updateUserPhoto: SUCCESS")
                    onSuccess()
                }
                .addOnFailureListener {
                    Timber.d("Database request: updateUserPhoto: FAILURE")
                    toastException(it)
                }
    }

    fun updateUsername(name: String, onSuccess: () -> Unit) {
        Timber.d("Database request: updateUsername: STARTED")
        val map = hashMapOf<String, Any>(CHILD_USERNAME to name)
        refDatabase.child(NODE_USERS).child(USER.id).updateChildren(map)
                .addOnSuccessListener {
                    USER.username = name
                    Timber.d("Database request: updateUsername: SUCCESS")
                    onSuccess()
                }
                .addOnFailureListener {
                    Timber.d("Database request: updateUsername: FAILURE")
                    toastException(it)
                }
    }

    fun updateImageRating(name: String, onComplete: () -> Unit) {
        Timber.d("Database request: updateImageRating: STARTED")
        refDatabase.child(NODE_IMAGES).child(name).singleDataChange { snapshot ->
            val image = snapshot.toImage()
            image.rating += 1
            snapshot.ref.child(CHILD_RATING).setValue(image.rating)
            Timber.d("Database request: updateImageRating: COMPLETE")
            updateUserRating(image.owner_id) {
                onComplete()
            }
        }
    }

    fun deleteImage(name: String, onSuccess: () -> Unit) {
        Timber.d("Database request: deleteImage: STARTED")
        refDatabase.child(NODE_IMAGES).child(name).setValue(null)
                .addOnSuccessListener {
                    Timber.d("Database request: deleteImage: SUCCESS")
                    onSuccess()
                }
                .addOnFailureListener {
                    Timber.d("Database request: deleteImage: FAILURE")
                    toastException(it)
                }
    }

    /**
     * Updates the rating of the user whose image was voted for
     * @param id image owner user id
     */
    private fun updateUserRating(id: String, onComplete: () -> Unit) {
        Timber.d("Database request: updateUserRating: STARTED")
        refDatabase.child(NODE_USERS).child(id).singleDataChange { snapshot ->
            val user = snapshot.toUser()
            user.rating += 1
            snapshot.ref.child(CHILD_RATING).setValue(user.rating)
            Timber.d("Database request: updateUserRating: COMPLETE")
            onComplete()
        }
    }

    private fun loadAddedUserImage(name: String, onComplete: (Image) -> Unit) {
        Timber.d("Database request: loadAddedUserImage: STARTED")
        refDatabase.child(NODE_IMAGES).child(name).singleDataChange { snapshot ->
            Timber.d("Database request: loadAddedUserImage: COMPLETE")
            onComplete(snapshot.toImage())
        }
    }

    private companion object {
        const val NODE_IMAGES = "images"
        const val NODE_USERS = "users"

        const val CHILD_OWNER_NAME = "owner_name"
        const val CHILD_TIMESTAMP = "timestamp"
        const val CHILD_OWNER_ID = "owner_id"
        const val CHILD_USERNAME = "username"
        const val CHILD_RATING = "rating"
        const val CHILD_LOGIN = "login"
        const val CHILD_PHOTO = "photo"
        const val CHILD_NAME = "name"
        const val CHILD_URL = "url"
        const val CHILD_ID = "id"
    }
}