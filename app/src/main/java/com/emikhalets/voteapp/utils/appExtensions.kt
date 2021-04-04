package com.emikhalets.voteapp.utils

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.recyclerview.widget.RecyclerView
import com.emikhalets.voteapp.BuildConfig
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.model.entities.User
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

fun initLogger() {
    if (BuildConfig.DEBUG) Timber.plant(object : Timber.DebugTree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            super.log(priority, "___APPLICATION___", message, t)
        }
    })
}

// Navigation Component

fun navigate(
        action: Int,
        args: Bundle? = null,
        options: NavOptions? = null,
        extras: Navigator.Extras? = null,
) = ACTIVITY.navController.navigate(action, args, options, extras)

fun popBackStack(destination: Int? = null, inclusive: Boolean = false) {
    if (destination == null) ACTIVITY.navController.popBackStack()
    else ACTIVITY.navController.popBackStack(destination, inclusive)
}

// Toasts

fun toast(message: String) = Toast.makeText(ACTIVITY, message, Toast.LENGTH_SHORT).show()

fun toastLong(message: String) = Toast.makeText(ACTIVITY, message, Toast.LENGTH_LONG).show()

fun toastException(exception: Exception?) {
    when (exception) {
        is FirebaseAuthInvalidUserException -> toast(ACTIVITY.getString(R.string.app_toast_login_not_exist))
        is FirebaseAuthRecentLoginRequiredException -> toastLong(ACTIVITY.getString(R.string.app_toast_need_relog_change_pass))
        is FirebaseAuthInvalidCredentialsException -> toast(ACTIVITY.getString(R.string.app_toast_invalid_pass))
        is FirebaseAuthUserCollisionException -> toast(ACTIVITY.getString(R.string.app_toast_login_busy))
        else -> {
            exception?.printStackTrace()
            Toast.makeText(ACTIVITY, exception?.message.toString(), Toast.LENGTH_LONG).show()
        }
    }
}

// Image Loaders

fun ImageView.loadImage(
        url: String,
        placeholder: Int = R.drawable.placeholder_default,
) {
    val image = if (url.isEmpty()) "no image" else url
    Picasso.get().load(image).placeholder(placeholder).error(placeholder).into(this)
}

// View extensions

fun hideKeyboard() {
    val imm = ACTIVITY.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(ACTIVITY.window.decorView.windowToken, 0)
}

// TODO Is this approach right? (moved the recyclerView scroll into extensions)
fun RecyclerView.scrollToTop() {
    CoroutineScope(Dispatchers.Main).launch {
        delay(100)
        this@scrollToTop.smoothScrollToPosition(0)
    }
}

// Dependency injection

inline fun <reified T : ViewModel> Fragment.injectViewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProvider(this, factory).get(T::class.java)
}

// Firebase extensions

fun DataSnapshot.toUser(): User = this.getValue(User::class.java) ?: User()

fun DataSnapshot.toImage(): Image = this.getValue(Image::class.java) ?: Image()

fun userId(): String = Firebase.auth.currentUser?.uid.toString()