package com.emikhalets.voteapp.network.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseVoting {
    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("error_code")
    @Expose
    var errorCode = 0

    @SerializedName("error_msg")
    @Expose
    var errorMsg: String? = null

    @SerializedName("error_ucode")
    @Expose
    var errorUcode: String? = null

    @SerializedName("data")
    @Expose
    var data: DataVoting? = null
}