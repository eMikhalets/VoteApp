package com.emikhalets.voteapp.model.entities

data class Image(
        val id: String,
        val url: String,
        val rating: Int,
        val ownerId: String,
        val ownerName: String
)