package com.emikhalets.voteapp.test

import android.widget.ImageView
import coil.load
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.model.entities.Image

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

fun ImageView.loadMock(url: Int) {
    load(url) { placeholder(R.drawable.placeholder_default) }
}