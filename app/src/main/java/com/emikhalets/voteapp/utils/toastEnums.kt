package com.emikhalets.voteapp.utils

enum class LoginToast {
    SUCCESS,
    EMPTY_FIELDS,
    INVALID_EMAIL,
    INVALID_PASS
}

enum class RegisterToast {
    SUCCESS,
    EMPTY_FIELDS,
    INVALID_EMAIL,
    INVALID_PASS,
    PASS_MISMATCH
}

enum class ChangePassToast {
    SUCCESS,
    EMPTY_FIELDS,
    INVALID_PASS,
    PASS_MISMATCH
}

enum class ChangeNameToast {
    SUCCESS,
    EMPTY_FIELDS
}