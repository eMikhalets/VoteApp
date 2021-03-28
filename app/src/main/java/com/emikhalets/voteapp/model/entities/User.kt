package com.emikhalets.voteapp.model.entities

data class User(
        val id: String,
        val username: String,
        val password: String,
        val photo: String,
        val rating: Int
)