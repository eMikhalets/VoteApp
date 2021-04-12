package com.emikhalets.voteapp.view

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.drawerlayout.widget.DrawerLayout
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.utils.loadImage
import com.emikhalets.voteapp.utils.navigate
import com.emikhalets.voteapp.utils.popBackStack
import com.emikhalets.voteapp.utils.username
import com.emikhalets.voteapp.view.screens.HomeFragmentDirections
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader

class AppDrawer(private val activity: MainActivity) {

    private lateinit var drawer: Drawer
    private lateinit var header: AccountHeader
    private lateinit var layout: DrawerLayout
    private lateinit var profile: ProfileDrawerItem

    private val itemClickListener = object : Drawer.OnDrawerItemClickListener {
        override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
            when (position) {
                1 -> navigate(activity, HomeFragmentDirections.actionHomeToProfile())
                2 -> navigate(activity, HomeFragmentDirections.actionHomeToUserImages())
                3 -> navigate(activity, HomeFragmentDirections.actionHomeToVoting())
                4 -> navigate(activity, HomeFragmentDirections.actionHomeToTopImages())
                5 -> navigate(activity, HomeFragmentDirections.actionHomeToTopUsers())
                else -> {
                }
            }
            return true
        }
    }

    fun create() {
        initLoader()
        createHeader()
        createDrawer()
        layout = drawer.drawerLayout
    }

    fun enableDrawer() {
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        drawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true
        layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        activity.toolbar.setNavigationOnClickListener { drawer.openDrawer() }
    }

    fun hideDrawer() {
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        drawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false
        layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    fun disableDrawer() {
        drawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        activity.toolbar.setNavigationOnClickListener { popBackStack(activity) }
    }

    fun updateHeader(user: User) {
        profile.withName(user.username)
                .withEmail(activity.getString(R.string.drawer_rating, user.rating))
                .withIdentifier(0)
        if (user.photo.isNotEmpty() && user.photo != "null") profile.withIcon(user.photo)
        else profile.withIcon(R.drawable.placeholder_user)
        header.updateProfile(profile)
    }

    private fun initLoader() {
        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable, tag: String?) {
                if (uri.toString().isNotEmpty() && uri.toString() != "null") {
                    imageView.loadImage(uri.toString(), R.drawable.placeholder_user)
                }
            }
        })
    }

    private fun createHeader() {
        profile = ProfileDrawerItem()
                .withName(username())
                .withIcon(R.drawable.placeholder_user)
                .withEmail(activity.getString(R.string.drawer_rating, 0))
                .withIdentifier(0)
        header = AccountHeaderBuilder()
                .withActivity(activity)
                .withSelectionListEnabled(false)
                .withHeaderBackground(R.drawable.background_drawer_header)
                .addProfiles(profile)
                .build()
    }

    private fun createDrawer() {
        drawer = DrawerBuilder()
                .withActivity(activity)
                .withToolbar(activity.toolbar)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(header)
                .withSelectedItem(-1)
                .addDrawerItems(
                        createItem(0, R.string.drawer_profile, R.drawable.ic_profile),
                        createItem(1, R.string.drawer_user_images, R.drawable.ic_image),
                        createItem(2, R.string.drawer_voting, R.drawable.ic_vote),
                        createItem(3, R.string.drawer_top_images, R.drawable.ic_top_images),
                        createItem(4, R.string.drawer_top_users, R.drawable.ic_top_users)
                )
                .withOnDrawerItemClickListener(itemClickListener)
                .build()
    }

    private fun createItem(identifier: Long, nameRes: Int, iconRes: Int): PrimaryDrawerItem {
        return PrimaryDrawerItem()
                .withIdentifier(identifier)
                .withName(nameRes)
                .withIcon(iconRes)
                .withSelectable(false)
    }
}