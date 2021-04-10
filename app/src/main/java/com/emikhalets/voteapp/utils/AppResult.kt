package com.emikhalets.voteapp.utils

sealed class AppResult<out T : Any> {

    class Success<out T : Any>(val data: T) : AppResult<T>()
    class Error(val message: String) : AppResult<Nothing>()
}