package com.emikhalets.voteapp.model.firebase

import com.emikhalets.voteapp.utils.USER
import com.emikhalets.voteapp.utils.USER_ID
import com.emikhalets.voteapp.utils.singleDataChange
import com.emikhalets.voteapp.utils.toastException

class FirebaseDatabaseRepository {

    inline fun fillCurrentUserData(crossinline onComplete: () -> Unit = {}) {
        REF_DATABASE.child(NODE_USERS).child(USER_ID).singleDataChange {
            USER = it.toUser()
            onComplete()
        }
    }

    inline fun saveUserToDatabase(login: String, pass: String, crossinline onSuccess: () -> Unit) {
        val map = hashMapOf(
                CHILD_ID to USER_ID,
                CHILD_USERNAME to login,
                CHILD_PASSWORD to pass,
                CHILD_PHOTO to "",
                CHILD_RATING to 0
        )
        REF_DATABASE.child(NODE_USERS).child(USER_ID).setValue(map)
                .addOnFailureListener { toastException(it) }
                .addOnSuccessListener { onSuccess() }
    }
}