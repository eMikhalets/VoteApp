package com.emikhalets.voteapp.model.entities

data class User(
        var id: String = "",
        var username: String = "",
        var photo: String = "null",
        var rating: Int = 0
)