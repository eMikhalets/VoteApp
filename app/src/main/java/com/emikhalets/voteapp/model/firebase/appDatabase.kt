package com.emikhalets.voteapp.model.firebase

import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.utils.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue

const val NODE_IMAGES = "images"

const val CHILD_ID = "id"
const val CHILD_RATING = "rating"
const val CHILD_URL = "url"
const val CHILD_OWNER_ID = "owner_id"
const val CHILD_DATE = "date"

val REF_DATABASE = FirebaseDatabase.getInstance().reference

fun DataSnapshot.toUser(): User = this.getValue(User::class.java) ?: User()

fun DataSnapshot.toImage(): Image = this.getValue(Image::class.java) ?: Image()

fun getNewKeyFromImagesNode(): String {
    return REF_DATABASE.child(NODE_IMAGES).child(USER_ID).push().key.toString()
}

inline fun loadUserImages(crossinline function: (images: List<Image>) -> Unit) {
    REF_DATABASE.child(NODE_IMAGES).child(USER_ID).singleDataChange { snapshot ->
        val images = mutableListOf<Image>()
        snapshot.children.forEach { images.add(it.toImage()) }
        function(images)
    }
}

fun saveImageToDatabase(url: String, key: String) {
    val map = hashMapOf(
            CHILD_ID to key,
            CHILD_URL to url,
            CHILD_RATING to 0,
            CHILD_OWNER_ID to USER_ID,
//            CHILD_OWNER_NAME to USER.username,
            CHILD_DATE to ServerValue.TIMESTAMP
    )
    REF_DATABASE.child(NODE_IMAGES).child(USER_ID).child(key).setValue(map)
            .addOnFailureListener { toastException(it) }
            .addOnSuccessListener { popBackStack(R.id.homeFragment) }
}