package com.emikhalets.voteapp.network.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataProfile {
    @SerializedName("tester_name")
    @Expose
    var testerName: String? = null

    @SerializedName("login")
    @Expose
    var login: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null
}