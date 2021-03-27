package com.emikhalets.voteapp.network.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataToken {
    @SerializedName("user_token")
    @Expose
    var userToken: String? = null
}