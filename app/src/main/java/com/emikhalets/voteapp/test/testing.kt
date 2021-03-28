package com.emikhalets.voteapp.test

import android.widget.ImageView
import coil.load
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.model.entities.Image

fun createMockImages(): List<Image> {
    return listOf(
            Image("0", R.drawable.test_image_1.toString(), 5, "0", "mikeTyson"),
            Image("1", R.drawable.test_image_2.toString(), 10, "0", "barakObama"),
            Image("2", R.drawable.test_image_3.toString(), 8, "0", "jesus"),
            Image("3", R.drawable.test_image_4.toString(), 1, "0", "alibaba"),
            Image("4", R.drawable.test_image_5.toString(), 4, "0", "someone"),
            Image("5", R.drawable.test_image_6.toString(), 17, "0", "mainUser")
    )
}

fun ImageView.loadMock(url: Int) {
    load(url) { placeholder(R.drawable.placeholder_default) }
}