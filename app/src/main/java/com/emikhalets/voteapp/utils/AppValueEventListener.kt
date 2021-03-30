package com.emikhalets.voteapp.utils

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

inline fun Query.singleDataChange(
        crossinline action: (snapshot: DataSnapshot) -> Unit,
) = addSingleValueEventListener(onSingleDataChange = action)

inline fun Query.singleCancelled(
        crossinline action: (error: DatabaseError) -> Unit,
) = addSingleValueEventListener(onSingleCancelled = action)

inline fun Query.addSingleValueEventListener(
        crossinline onSingleDataChange: (snapshot: DataSnapshot) -> Unit = { _ -> },
        crossinline onSingleCancelled: (error: DatabaseError) -> Unit = { _ -> },
): ValueEventListener {
    val eventListener = object : ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {
            onSingleDataChange(snapshot)
        }

        override fun onCancelled(error: DatabaseError) {
            onSingleCancelled(error)
        }
    }
    addListenerForSingleValueEvent(eventListener)
    return eventListener
}