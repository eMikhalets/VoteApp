package com.emikhalets.voteapp.model.entities

data class User(
        var id: String = "",
        var username: String = "",
        var password: String = "",
        val photo: String = "",
        val rating: Int = 0
)