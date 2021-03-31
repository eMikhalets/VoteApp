package com.emikhalets.voteapp.test

import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.model.entities.User

fun createMockUsers(): List<User> {
    return listOf(
            User("0", "mikeTyson", R.drawable.test_photo_1.toString(), 50),
            User("1", "barakObama", R.drawable.test_photo_2.toString(), 10),
            User("2", "jesus", R.drawable.test_photo_3.toString(), 40),
            User("3", "alibaba", R.drawable.test_photo_4.toString(), 30),
            User("4", "someone", R.drawable.test_photo_5.toString(), 80),
            User("5", "mainUser", R.drawable.test_photo_6.toString(), 5),
            User("6", "mrPresident", R.drawable.test_photo_7.toString(), 100),
            User("7", "dartvader", R.drawable.test_photo_8.toString(), 12),
            User("8", "superman", R.drawable.test_photo_9.toString(), 65),
            User("9", "batman", R.drawable.test_photo_10.toString(), 27),
    )
}
