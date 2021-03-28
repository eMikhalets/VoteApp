package com.emikhalets.voteapp.view

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.drawerlayout.widget.DrawerLayout
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.utils.navigate
import com.emikhalets.voteapp.utils.popBackStack
import com.emikhalets.voteapp.utils.toast
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
            toast(position.toString())
            when (position) {
                1 -> navigate(R.id.action_home_to_profile)
                2 -> navigate(R.id.action_home_to_userImages)
                3 -> navigate(R.id.action_home_to_voting)
                4 -> navigate(R.id.action_home_to_topImages)
                5 -> navigate(R.id.action_home_to_topUsers)
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

    fun disableDrawer() {
        drawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false
        ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        ACTIVITY.toolbar.setNavigationOnClickListener { popBackStack() }
    }

//    fun updateHeader() {
//        profile.withName(USER.fullname)
//                .withEmail(USER.phone)
//                .withIcon(USER.photo_url)
//                .withIdentifier(200)
//        header.updateProfile(profile)
//    }

    private fun initLoader() {
        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable) {
//                imageView.downloadAndSetImage(uri.toString())
            }
        })
    }

    private fun createHeader() {
        profile = ProfileDrawerItem()
                .withName(R.string.test_user_name)
                .withIcon(R.drawable.placeholder_user)
                .withIdentifier(0)
        header = AccountHeaderBuilder()
                .withActivity(ACTIVITY)
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
                .withSelectable(true)
    }
}