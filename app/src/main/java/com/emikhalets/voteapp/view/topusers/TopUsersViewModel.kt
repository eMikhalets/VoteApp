package com.emikhalets.voteapp.view.topusers

import androidx.lifecycle.ViewModel
import com.emikhalets.voteapp.data.AppRepository
import com.emikhalets.voteapp.data.AppRepository.Companion.get

class TopUsersViewModel : ViewModel() {
    private val repository: AppRepository?
    private var userToken: String? = ""
    fun setUserToken(userToken: String?) {
        this.userToken = userToken
    }

    init {
        repository = get()
    }
}