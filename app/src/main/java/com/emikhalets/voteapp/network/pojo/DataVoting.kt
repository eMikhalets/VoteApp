package com.emikhalets.voteapp.network.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataVoting {
    @SerializedName("photos")
    @Expose
    var images: List<DataImage>? = null

    @SerializedName("vote_token")
    @Expose
    var voteToken: String? = null
}