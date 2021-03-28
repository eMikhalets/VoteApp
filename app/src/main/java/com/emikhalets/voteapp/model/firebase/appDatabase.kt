package com.emikhalets.voteapp.model.firebase

import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.utils.popBackStack
import com.emikhalets.voteapp.utils.toastException
import com.google.firebase.database.FirebaseDatabase

private const val NODE_USERS = "users"

private const val CHILD_ID = "id"
private const val CHILD_USERNAME = "username"
private const val CHILD_PASSWORD = "password"
private const val CHILD_PHOTO = "photo"
private const val CHILD_RATING = "rating"
private const val CHILD_IMAGES = "images"

private val REF_DATABASE = FirebaseDatabase.getInstance().reference
private val REF_USERS_ID = REF_DATABASE.child(NODE_USERS).child(USER_ID)

fun saveUserToDatabase() {
    REF_USERS_ID.setValue(USER)
            .addOnFailureListener { toastException(it) }
            .addOnSuccessListener { popBackStack(R.id.homeFragment) }
}