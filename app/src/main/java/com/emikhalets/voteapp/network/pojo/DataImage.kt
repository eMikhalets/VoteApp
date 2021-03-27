package com.emikhalets.voteapp.network.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataImage {
    @SerializedName("id")
    @Expose
    var id = 0

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("link")
    @Expose
    var link: String? = null

    @SerializedName("rate")
    @Expose
    var rate = 0
}