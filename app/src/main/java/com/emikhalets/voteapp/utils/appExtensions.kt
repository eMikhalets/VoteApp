package com.emikhalets.voteapp.utils

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.emikhalets.voteapp.BuildConfig
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.view.MainActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay
import timber.log.Timber

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

fun initLogger() {
    if (BuildConfig.DEBUG) Timber.plant(object : Timber.DebugTree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            super.log(priority, "___APPLICATION___", message, t)
        }
    })
}

fun Fragment.activity() = activity as MainActivity

// Navigation Component

fun navigate(activity: AppCompatActivity, direction: NavDirections) =
        Navigation.findNavController(activity, R.id.fragment_container).navigate(direction)

fun popBackStack(activity: AppCompatActivity) =
        Navigation.findNavController(activity, R.id.fragment_container).popBackStack()

fun Fragment.navigate(direction: NavDirections) = findNavController().navigate(direction)

fun Fragment.navigate(direction: NavDirections, sharedElement: Pair<View, String>) {
    val extras = FragmentNavigatorExtras(sharedElement)
    findNavController().navigate(direction, extras)
}

fun Fragment.popBackStack(destination: Int? = null, inclusive: Boolean = false) {
    if (destination == null) findNavController().popBackStack()
    else findNavController().popBackStack(destination, inclusive)
}

// Toasts

fun Fragment.toast(stringRes: Int) =
        Toast.makeText(requireContext(), stringRes, Toast.LENGTH_SHORT).show()

fun Fragment.toast(message: String) =
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

fun Fragment.toastLong(stringRes: Int) =
        Toast.makeText(requireContext(), stringRes, Toast.LENGTH_LONG).show()

fun Fragment.toastLong(message: String) =
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

//fun toastException(exception: Exception?) {
//    when (exception) {
//        is FirebaseAuthInvalidUserException -> toast(ACTIVITY.getString(R.string.app_toast_login_not_exist))
//        is FirebaseAuthRecentLoginRequiredException -> toastLong(ACTIVITY.getString(R.string.app_toast_need_relog_change_pass))
//        is FirebaseAuthInvalidCredentialsException -> toast(ACTIVITY.getString(R.string.app_toast_invalid_pass))
//        is FirebaseAuthUserCollisionException -> toast(ACTIVITY.getString(R.string.app_toast_login_busy))
//        else -> {
//            exception?.printStackTrace()
//            Toast.makeText(ACTIVITY, exception?.message.toString(), Toast.LENGTH_LONG).show()
//        }
//    }
//}

// Image Loaders

fun ImageView.loadImage(
        url: String,
        placeholder: Int = R.drawable.placeholder_default,
) {
    val image = if (url.isEmpty()) "null" else url
    Picasso.get().load(image).placeholder(placeholder).error(placeholder).into(this)
}

// View extensions

fun Fragment.hideKeyboard() {
    val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(requireActivity().window.decorView.windowToken, 0)
}

// TODO Is this approach right? (moved the recyclerView scroll into extensions)
fun RecyclerView.scrollToTop(fragment: Fragment) {
    fragment.lifecycleScope.launchWhenResumed {
        delay(100)
        this@scrollToTop.smoothScrollToPosition(0)
    }
}

fun RecyclerView.handleTransition(fragment: Fragment) {
    fragment.postponeEnterTransition()
    viewTreeObserver.addOnPreDrawListener {
        fragment.startPostponedEnterTransition()
        true
    }
}

fun View.animShow(duration: Long = 400, start: () -> Unit = {}) {
    alpha = 0f
    scaleX = 0f
    scaleY = 0f
    val listener = AppAnimatorListener(start = { start() })
    animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(duration)
            .setInterpolator(AccelerateInterpolator()).setListener(listener).start()
}

fun View.animHide(duration: Long = 0, end: () -> Unit = {}) {
    alpha = 1f
    scaleX = 1f
    scaleY = 1f
    val listener = AppAnimatorListener(end = { end() })
    animate().alpha(0f).scaleX(0f).scaleY(0f).setDuration(duration)
            .setInterpolator(AccelerateInterpolator()).setListener(listener).start()
}

// Dependency injection

inline fun <reified T : ViewModel> Fragment.injectViewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProvider(this, factory).get(T::class.java)
}

// Firebase extensions

fun DataSnapshot.toUser(): User = this.getValue(User::class.java) ?: User()

fun DataSnapshot.toImage(): Image = this.getValue(Image::class.java) ?: Image()

fun userId(): String = Firebase.auth.currentUser?.uid.toString()

fun username(): String = Firebase.auth.currentUser?.displayName.toString()

fun userPhoto(): String = Firebase.auth.currentUser?.photoUrl.toString()

fun userEmail(): String = Firebase.auth.currentUser?.email.toString()