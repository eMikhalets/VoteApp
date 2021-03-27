package com.emikhalets.voteapp.utils

import android.os.Bundle

fun navigate(action: Int, args: Bundle = Bundle.EMPTY) {
    if (args.isEmpty) ACTIVITY.navController.navigate(action)
    else ACTIVITY.navController.navigate(action, args)
}