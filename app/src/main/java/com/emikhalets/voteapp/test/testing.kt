package com.emikhalets.voteapp.test

import android.widget.ImageView
import coil.load
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.model.entities.User

fun createMockImages(): List<Image> {
    return listOf(
            Image("0", R.drawable.test_image_1.toString(), 5, "0", "mikeTyson", 1),
            Image("1", R.drawable.test_image_2.toString(), 10, "0", "barakObama", 2),
            Image("2", R.drawable.test_image_3.toString(), 8, "0", "jesus", 3),
            Image("3", R.drawable.test_image_4.toString(), 17, "0", "alibaba", 4),
            Image("4", R.drawable.test_image_5.toString(), 4, "0", "someone", 5),
            Image("5", R.drawable.test_image_6.toString(), 17, "0", "mainUser", 6)
    )
}

fun createMockUsers(): List<User> {
    return listOf(
            User("0", "mikeTyson", "", R.drawable.test_photo_1.toString(), 50),
            User("1", "barakObama", "", R.drawable.test_photo_2.toString(), 10),
            User("2", "jesus", "", R.drawable.test_photo_3.toString(), 40),
            User("3", "alibaba", "", R.drawable.test_photo_4.toString(), 30),
            User("4", "someone", "", R.drawable.test_photo_5.toString(), 80),
            User("5", "mainUser", "", R.drawable.test_photo_6.toString(), 5),
            User("6", "mrPresident", "", R.drawable.test_photo_7.toString(), 100),
            User("7", "dartvader", "", R.drawable.test_photo_8.toString(), 12),
            User("8", "superman", "", R.drawable.test_photo_9.toString(), 65),
            User("9", "batman", "", R.drawable.test_photo_10.toString(), 27),
    )
}

fun createMockUser(): User {
    return User("0", "mikeTyson", "", R.drawable.test_photo_1.toString(), 50)
}

fun ImageView.loadMock(url: Int) {
    load(url) { placeholder(R.drawable.placeholder_default) }
}