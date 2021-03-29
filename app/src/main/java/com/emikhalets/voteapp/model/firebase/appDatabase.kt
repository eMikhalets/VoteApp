package com.emikhalets.voteapp.model.firebase

import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.utils.popBackStack
import com.emikhalets.voteapp.utils.singleDataChange
import com.emikhalets.voteapp.utils.toastException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue

const val NODE_USERS = "users"
const val NODE_IMAGES = "images"

const val CHILD_ID = "id"
const val CHILD_USERNAME = "username"
const val CHILD_PASSWORD = "password"
const val CHILD_PHOTO = "photo"
const val CHILD_RATING = "rating"
const val CHILD_URL = "url"
const val CHILD_OWNER_ID = "owner_id"
const val CHILD_OWNER_NAME = "owner_name"
const val CHILD_DATE = "date"

val REF_DATABASE = FirebaseDatabase.getInstance().reference

fun initCurrentUser() {
    REF_DATABASE.child(NODE_USERS).child(USER_ID).singleDataChange {
        USER = it.getValue(User::class.java) ?: User()
    }
}

fun getNewKeyFromImagesNode(): String {
    return REF_DATABASE.child(NODE_IMAGES).child(USER_ID).push().key.toString()
}

fun saveUserToDatabase() {
    val map = hashMapOf(
            CHILD_ID to USER_ID,
            CHILD_USERNAME to USER.username,
            CHILD_PASSWORD to USER.password,
            CHILD_PHOTO to USER.photo,
            CHILD_RATING to 0
    )
    REF_DATABASE.child(NODE_USERS).child(USER_ID).setValue(map)
            .addOnFailureListener { toastException(it) }
            .addOnSuccessListener { popBackStack(R.id.homeFragment) }
}

fun saveImageToDatabase(url: String, key: String) {
    val map = hashMapOf(
            CHILD_ID to key,
            CHILD_URL to url,
            CHILD_RATING to 0,
            CHILD_OWNER_ID to USER_ID,
            CHILD_OWNER_NAME to USER.username,
            CHILD_DATE to ServerValue.TIMESTAMP
    )
    REF_DATABASE.child(NODE_IMAGES).child(USER_ID).child(key).setValue(map)
            .addOnFailureListener { toastException(it) }
            .addOnSuccessListener { popBackStack(R.id.homeFragment) }
}