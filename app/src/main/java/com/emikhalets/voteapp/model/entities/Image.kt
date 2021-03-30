package com.emikhalets.voteapp.model.entities

data class Image(
        var name: String = "",
        var url: String = "",
        var rating: Int = 0,
        var ownerId: String = "",
        var ownerName: String = "",
        var timestamp: Long = 0
)