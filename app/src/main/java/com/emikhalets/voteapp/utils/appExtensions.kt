package com.emikhalets.voteapp.utils

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import coil.load
import com.emikhalets.voteapp.R
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException

fun navigate(action: Int, args: Bundle = Bundle.EMPTY) {
    if (args.isEmpty) ACTIVITY.navController.navigate(action)
    else ACTIVITY.navController.navigate(action, args)
}

fun popBackStack(destination: Int? = null, inclusive: Boolean = false) {
    if (destination == null) ACTIVITY.navController.popBackStack()
    else ACTIVITY.navController.popBackStack(destination, inclusive)
}

fun hideKeyboard() {
    val imm = ACTIVITY.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(ACTIVITY.window.decorView.windowToken, 0)
}

fun toast(message: String) {
    Toast.makeText(ACTIVITY, message, Toast.LENGTH_SHORT).show()
}

fun toastException(exception: Exception?) {
    when (exception) {
        is FirebaseAuthInvalidUserException -> toast(ACTIVITY.getString(R.string.app_toast_login_not_exist))
        is FirebaseAuthInvalidCredentialsException -> toast(ACTIVITY.getString(R.string.app_toast_invalid_pass))
        is FirebaseAuthUserCollisionException -> toast(ACTIVITY.getString(R.string.app_toast_login_busy))
        else -> {
            exception?.printStackTrace()
            Toast.makeText(ACTIVITY, exception?.message.toString(), Toast.LENGTH_LONG).show()
        }
    }
}

fun ImageView.loadImage(
        url: String,
        placeholder: Int = R.drawable.placeholder_default,
        crossfade: Int = 0
) {
    load(url) {
        crossfade(crossfade)
        placeholder(placeholder)
        fallback(placeholder)
        error(placeholder)
    }
}
