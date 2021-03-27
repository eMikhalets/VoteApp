package com.emikhalets.voteapp.utils

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

fun navigate(action: Int, args: Bundle = Bundle.EMPTY) {
    if (args.isEmpty) ACTIVITY.navController.navigate(action)
    else ACTIVITY.navController.navigate(action, args)
}

fun popBackStack(destination: Int? = null) {
    if (destination == null) ACTIVITY.navController.popBackStack()
    else ACTIVITY.navController.popBackStack(destination, false)
}

fun hideKeyboard() {
    val imm = ACTIVITY.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(ACTIVITY.window.decorView.windowToken, 0)
}

fun toast(message: String) {
    Toast.makeText(ACTIVITY, message, Toast.LENGTH_SHORT).show()
}

fun toastException(exception: Exception?) {
    exception?.printStackTrace()
    Toast.makeText(ACTIVITY, exception?.message.toString(), Toast.LENGTH_LONG).show()
}