package com.emikhalets.voteapp.view

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.drawerlayout.widget.DrawerLayout
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.utils.*
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader

class AppDrawer {

    private lateinit var drawer: Drawer
    private lateinit var header: AccountHeader
    private lateinit var layout: DrawerLayout
    private lateinit var profile: ProfileDrawerItem

    private val itemClickListener = object : Drawer.OnDrawerItemClickListener {
        override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
            when (position) {
                1 -> navigateOld(R.id.action_home_to_profile)
                2 -> navigateOld(R.id.action_home_to_userImages)
                3 -> navigateOld(R.id.action_home_to_voting)
                4 -> navigateOld(R.id.action_home_to_topImages)
                5 -> navigateOld(R.id.action_home_to_topUsers)
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
        ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        drawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true
        layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        ACTIVITY.toolbar.setNavigationOnClickListener { drawer.openDrawer() }
    }

    fun hideDrawer() {
        ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        drawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false
        layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    fun disableDrawer() {
        drawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false
        ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        ACTIVITY.toolbar.setNavigationOnClickListener { popBackStackOld() }
    }

    fun updateHeader() {
        profile.withName(USER.username)
                .withEmail(ACTIVITY.getString(R.string.drawer_rating, USER.rating))
                .withIdentifier(0)
        if (USER.photo.isNotEmpty() && USER.photo != "null") profile.withIcon(USER.photo)
        else profile.withIcon(R.drawable.placeholder_user)
        header.updateProfile(profile)
    }

    private fun initLoader() {
        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable) {
                if (uri.toString().isNotEmpty() && uri.toString() != "null") {
                    imageView.loadImage(uri.toString(), R.drawable.placeholder_user)
                }
            }
        })
    }

    private fun createHeader() {
        profile = ProfileDrawerItem()
                .withName(USER.username)
                .withIcon(R.drawable.placeholder_user)
                .withEmail(ACTIVITY.getString(R.string.drawer_rating, USER.rating))
                .withIdentifier(0)
        header = AccountHeaderBuilder()
                .withActivity(ACTIVITY)
                .withSelectionListEnabled(false)
                .withHeaderBackground(R.drawable.background_drawer_header)
                .addProfiles(profile)
                .build()
    }

    private fun createDrawer() {
        drawer = DrawerBuilder()
                .withActivity(ACTIVITY)
                .withToolbar(ACTIVITY.toolbar)
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