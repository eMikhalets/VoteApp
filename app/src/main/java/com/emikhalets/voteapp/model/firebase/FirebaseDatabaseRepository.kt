package com.emikhalets.voteapp.model.firebase

import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.utils.USER_ID
import com.emikhalets.voteapp.utils.toastException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

class FirebaseDatabaseRepository {

    val refDatabase = FirebaseDatabase.getInstance().reference

    fun DataSnapshot.toUser(): User = this.getValue(User::class.java) ?: User()

    fun DataSnapshot.toImage(): Image = this.getValue(Image::class.java) ?: Image()

    inline fun saveUserToDatabase(login: String, crossinline onSuccess: () -> Unit) {
        val map = hashMapOf(
                CHILD_ID to USER_ID,
                CHILD_USERNAME to login,
                CHILD_PHOTO to "",
                CHILD_RATING to 0
        )
        refDatabase.child(NODE_USERS).child(USER_ID).setValue(map)
                .addOnFailureListener { toastException(it) }
                .addOnSuccessListener { onSuccess() }
    }

    companion object {
        const val NODE_IMAGES = "images"
        const val NODE_USERS = "users"

        const val CHILD_OWNER_NAME = "owner_name"
        const val CHILD_OWNER_ID = "owner_id"
        const val CHILD_USERNAME = "username"
        const val CHILD_RATING = "rating"
        const val CHILD_PHOTO = "photo"
        const val CHILD_DATE = "date"
        const val CHILD_URL = "url"
        const val CHILD_ID = "id"
    }
}