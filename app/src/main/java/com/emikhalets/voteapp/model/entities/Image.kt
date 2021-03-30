package com.emikhalets.voteapp.model.entities

data class Image(
        var name: String = "",
        var url: String = "",
        var rating: Int = 0,
        var owner_id: String = "",
        var owner_name: String = "",
        var timestamp: Long = 0
)