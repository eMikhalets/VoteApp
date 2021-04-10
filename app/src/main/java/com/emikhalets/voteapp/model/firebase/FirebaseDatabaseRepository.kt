package com.emikhalets.voteapp.model.firebase

import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.utils.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ServerValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class FirebaseDatabaseRepository @Inject constructor(
        private val refDatabase: DatabaseReference,
) {

    /**
     * Method for adding a listener for changes to the data of the current user
     * @return Link to the current user in the database
     */
    fun listenUserDataChanges(): DatabaseReference {
        Timber.d("Database request: listenUserDataChanges: STARTED")
        return refDatabase.child(NODE_USERS).child(userId())
    }

    /**
     * Method for adding a listener for changes to the data of the current user images
     * @return Link to the user images in the database
     */
    fun listenUserImagesChanges(): Query {
        Timber.d("Database request: listenUserImagesChanges: STARTED")
        return refDatabase.child(NODE_IMAGES).orderByChild(CHILD_OWNER_ID).equalTo(userId())
    }

    /**
     * Loads the data of the current user.
     * If the user exists in the database, callback returns a [Result.Success(true)].
     * Else, it returns [Result.Success(true)]
     * @param complete Callback
     */
    suspend fun loadUserData(complete: (AppResult<Boolean>) -> Unit) = withContext(Dispatchers.IO) {
        Timber.d("Database request: loadUserData: STARTED")
        refDatabase.child(NODE_USERS).child(userId()).singleDataChange { snapshot ->
            if (snapshot.exists()) {
                Timber.d("Database request: loadUserData: COMPLETE (User exist)")
                complete(AppResult.Success(true))
            } else {
                Timber.d("Database request: loadUserData: COMPLETE (User not exist)")
                complete(AppResult.Error("User not exist in database"))
            }
        }
    }

    /**
     * Loads information from the database for all images and sorts by upload date.
     * Callback returns a [List]<[Image]>
     * @param complete Callback
     */
    suspend fun loadLatestImages(complete: (AppResult<List<Image>>) -> Unit) = withContext(Dispatchers.IO) {
        Timber.d("Database request: loadLatestImages: STARTED")
        refDatabase.child(NODE_IMAGES).orderByChild(CHILD_TIMESTAMP).singleDataChange { snapshot ->
            val list = mutableListOf<Image>()
            snapshot.children.forEach { list.add(it.toImage()) }
            Timber.d("Database request: loadLatestImages: COMPLETE")
            if (list.isEmpty()) complete(AppResult.Error(""))
            else complete(AppResult.Success(list))
        }
    }

    /**
     * Loads information from the database for all images and sorts by rating.
     * Callback returns a [List]<[Image]>
     * @param complete Callback
     */
    suspend fun loadTopImages(complete: (AppResult<List<Image>>) -> Unit) = withContext(Dispatchers.IO) {
        Timber.d("Database request: loadTopImages: STARTED")
        refDatabase.child(NODE_IMAGES).orderByChild(CHILD_RATING).singleDataChange { snapshot ->
            val list = mutableListOf<Image>()
            snapshot.children.forEach { list.add(it.toImage()) }
            Timber.d("Database request: loadTopImages: COMPLETE")
            if (list.isEmpty()) complete(AppResult.Error(""))
            else complete(AppResult.Success(list))
        }
    }

    /**
     * Loads information from the database for all images.
     * Called [complete] when the server responds to a request. Callback returns a [List]<[Image]>
     * @param complete Callback
     */
    suspend fun loadAllImages(complete: (AppResult<List<Image>>) -> Unit) = withContext(Dispatchers.IO) {
        Timber.d("Database request: loadAllImages: STARTED")
        refDatabase.child(NODE_IMAGES).singleDataChange { snapshot ->
            val list = mutableListOf<Image>()
            snapshot.children.forEach { list.add(it.toImage()) }
            Timber.d("Database request: loadAllImages: COMPLETE")
            if (list.isEmpty()) complete(AppResult.Error(""))
            else complete(AppResult.Success(list))
        }
    }

    /**
     * Loads information from a database of the 30 highest rated users.
     * Callback returns a [List]<[User]>
     * @param complete Callback
     */
    suspend fun loadTopUsers(complete: (AppResult<List<User>>) -> Unit) = withContext(Dispatchers.IO) {
        Timber.d("Database request: loadTopUsers: STARTED")
        refDatabase.child(NODE_USERS).orderByChild(CHILD_RATING).limitToLast(30)
                .singleDataChange { snapshot ->
                    val list = mutableListOf<User>()
                    snapshot.children.forEach { list.add(it.toUser()) }
                    Timber.d("Database request: loadTopUsers: COMPLETE")
                    if (list.isEmpty()) complete(AppResult.Error(""))
                    else complete(AppResult.Success(list))
                }
    }

    /**
     * Saves the user info to the database.
     * If request is successful, callback returns true, else returns exception message
     * @param email User email
     * @param complete Callback
     */
    suspend fun saveUser(email: String, complete: (AppResult<Boolean>) -> Unit) = withContext(Dispatchers.IO) {
        Timber.d("Database request: saveUser: STARTED")
        val map = hashMapOf(
                CHILD_ID to userId(),
                CHILD_EMAIL to email,
                CHILD_USERNAME to username(),
                CHILD_PHOTO to "null",
                CHILD_RATING to 0
        )
        refDatabase.child(NODE_USERS).child(userId()).setValue(map)
                .addOnSuccessListener {
                    Timber.d("Database request: saveUser: SUCCESS")
                    complete(AppResult.Success(true))
                }
                .addOnFailureListener {
                    Timber.d("Database request: saveUser: FAILURE")
                    it.printStackTrace()
                    complete(AppResult.Error(it.message.toString()))
                }
    }

    /**
     * Save the image uploaded by the user to the database.
     * If request is successful, callback returns true, else returns exception message
     * @param name Name of the saved image
     * @param url Url of the saved image in Firebase Cloud Storage
     * @param complete Callback
     */
    suspend fun saveUserImage(name: String, url: String, complete: (AppResult<Boolean>) -> Unit) = withContext(Dispatchers.IO) {
        Timber.d("Database request: saveImage: STARTED")
        val map = hashMapOf(
                CHILD_NAME to name,
                CHILD_URL to url,
                CHILD_RATING to 0,
                CHILD_OWNER_ID to userId(),
                CHILD_OWNER_NAME to username(),
                CHILD_TIMESTAMP to ServerValue.TIMESTAMP
        )
        refDatabase.child(NODE_IMAGES).child(name).setValue(map)
                .addOnSuccessListener {
                    Timber.d("Database request: saveImage: SUCCESS")
                    complete(AppResult.Success(true))
                }
                .addOnFailureListener {
                    Timber.d("Database request: saveImage: FAILURE")
                    it.printStackTrace()
                    complete(AppResult.Error(it.message.toString()))
                }
    }

    /**
     * Updates the link to the current user's profile photo in the database.
     * If request is successful, callback returns true, else returns exception message
     * @param url Url of the updated profile photo in Firebase Cloud Storage
     * @param complete Callback
     */
    suspend fun updateUserPhoto(url: String, complete: (AppResult<Boolean>) -> Unit) = withContext(Dispatchers.IO) {
        Timber.d("Database request: updateUserPhoto: STARTED")
        val map = hashMapOf<String, Any>(CHILD_PHOTO to url)
        refDatabase.child(NODE_USERS).child(userId()).updateChildren(map)
                .addOnSuccessListener {
                    Timber.d("Database request: updateUserPhoto: SUCCESS")
                    complete(AppResult.Success(true))
                }
                .addOnFailureListener {
                    Timber.d("Database request: updateUserPhoto: FAILURE")
                    it.printStackTrace()
                    complete(AppResult.Error(it.message.toString()))
                }
    }

    /**
     * Updates the username of the current user in the database.
     * If request is successful, callback returns true, else returns exception message
     * @param name New name of the current user
     * @param complete Callback
     */
    suspend fun updateUsername(name: String, complete: (AppResult<Boolean>) -> Unit) = withContext(Dispatchers.IO) {
        Timber.d("Database request: updateUsername: STARTED")
        val map = hashMapOf<String, Any>(CHILD_USERNAME to name)
        refDatabase.child(NODE_USERS).child(userId()).updateChildren(map)
                .addOnSuccessListener {
                    Timber.d("Database request: updateUsername: SUCCESS")
                    complete(AppResult.Success(true))
                }
                .addOnFailureListener {
                    Timber.d("Database request: updateUsername: FAILURE")
                    it.printStackTrace()
                    complete(AppResult.Error(it.message.toString()))
                }
    }

    /**
     * Updates the rating of an image in the database.
     * If request is successful, callback returns true, else returns exception message
     * @param name Image name
     * @param complete Callback
     */
    suspend fun updateImageRating(name: String, complete: (AppResult<Boolean>) -> Unit) = withContext(Dispatchers.IO) {
        Timber.d("Database request: updateImageRating: STARTED")
        refDatabase.child(NODE_IMAGES).child(name).singleDataChange { snapshot ->
            val image = snapshot.toImage()
            image.rating += 1
            snapshot.ref.child(CHILD_RATING).setValue(image.rating)
                    .addOnSuccessListener {
                        Timber.d("Database request: updateImageRating: SUCCESS")
                        refDatabase.child(NODE_USERS).child(image.owner_id).singleDataChange { snapshot ->
                            val user = snapshot.toUser()
                            user.rating += 1
                            snapshot.ref.child(CHILD_RATING).setValue(user.rating)
                                    .addOnSuccessListener {
                                        Timber.d("Database request: updateUserRating: SUCCESS")
                                        complete(AppResult.Success(true))
                                    }
                                    .addOnFailureListener {
                                        Timber.d("Database request: updateUserRating: FAILURE")
                                        it.printStackTrace()
                                        complete(AppResult.Error(it.message.toString()))
                                    }
                        }
                    }
                    .addOnFailureListener {
                        Timber.d("Database request: updateImageRating: FAILURE")
                        it.printStackTrace()
                        complete(AppResult.Error(it.message.toString()))
                    }
        }
    }

    /**
     * Updates the rating of the user whose image was voted for.
     * If request is successful, callback returns a true, else returns exception message
     * @param name Image name
     * @param complete Callback
     */
    suspend fun deleteImage(name: String, complete: (AppResult<Boolean>) -> Unit) = withContext(Dispatchers.IO) {
        Timber.d("Database request: deleteImage: STARTED")
        refDatabase.child(NODE_IMAGES).child(name).setValue(null)
                .addOnSuccessListener {
                    Timber.d("Database request: deleteImage: SUCCESS")
                    complete(AppResult.Success(true))
                }
                .addOnFailureListener {
                    Timber.d("Database request: deleteImage: FAILURE")
                    it.printStackTrace()
                    complete(AppResult.Error(it.message.toString()))
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
        const val CHILD_EMAIL = "email"
        const val CHILD_PHOTO = "photo"
        const val CHILD_NAME = "name"
        const val CHILD_URL = "url"
        const val CHILD_ID = "id"
    }
}