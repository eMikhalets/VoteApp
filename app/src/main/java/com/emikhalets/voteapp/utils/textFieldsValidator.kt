package com.emikhalets.voteapp.utils

inline fun validateLogIn(login: String, pass: String, crossinline onComplete: (LoginToast) -> Unit) {
    if (login.isNotEmpty() && pass.isNotEmpty()) {
        if (pass.length >= 6) {
            val pattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$".toRegex()
            if (login.matches(pattern)) {
                onComplete(LoginToast.SUCCESS)
            } else onComplete(LoginToast.INVALID_EMAIL)
        } else onComplete(LoginToast.INVALID_PASS)
    } else onComplete(LoginToast.EMPTY_FIELDS)
}

inline fun validateRegister(login: String, pass: String, passConf: String, crossinline onComplete: (RegisterToast) -> Unit) {
    if (login.isNotEmpty() && pass.isNotEmpty() && passConf.isNotEmpty()) {
        if (pass.length >= 6) {
            if (pass == passConf) {
                val pattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$".toRegex()
                if (login.matches(pattern)) {
                    onComplete(RegisterToast.SUCCESS)
                } else onComplete(RegisterToast.INVALID_EMAIL)
            } else onComplete(RegisterToast.PASS_MISMATCH)
        } else onComplete(RegisterToast.INVALID_PASS)
    } else onComplete(RegisterToast.EMPTY_FIELDS)
}