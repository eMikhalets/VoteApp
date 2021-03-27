package com.emikhalets.voteapp.network.pojo

class AppResponse {
    var dataString: String? = null
        private set
    var dataImages: List<DataImage>? = null
        private set
    var dataToken: DataToken? = null
        private set
    var dataVoting: DataVoting? = null
        private set
    var dataProfile: DataProfile? = null
        private set

    constructor() {}
    constructor(dataString: String?) {
        this.dataString = dataString
    }

    constructor(dataImages: List<DataImage>?) {
        this.dataImages = dataImages
    }

    constructor(dataToken: DataToken?) {
        this.dataToken = dataToken
    }

    constructor(dataVoting: DataVoting?) {
        this.dataVoting = dataVoting
    }

    constructor(dataProfile: DataProfile?) {
        this.dataProfile = dataProfile
    }
}