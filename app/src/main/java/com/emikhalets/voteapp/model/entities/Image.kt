package com.emikhalets.voteapp.model.entities

data class Image(
        var id: String = "",
        var url: String = "",
        var rating: Int = 0,
        var ownerId: String = "",
        var ownerName: String = "",
        var date: Long = 0
)