package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentTopUsersBinding
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.utils.toast
import com.emikhalets.voteapp.view.adapters.UsersAdapter
import com.emikhalets.voteapp.view.base.WithDrawerFragment
import com.emikhalets.voteapp.viewmodel.TopUsersViewModel

class TopUsersFragment : WithDrawerFragment(R.layout.fragment_top_users) {

    private val binding: FragmentTopUsersBinding by viewBinding()
    private val viewModel: TopUsersViewModel by viewModels()

    private val usersAdapter = UsersAdapter { onUserClick(it) }

    private fun onUserClick(user: User) {
        toast("clicked on ${user.username}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ACTIVITY.title = getString(R.string.top_users_title)
        initRecyclerView()
        initListeners()
        onViewLoaded()
    }

    private fun initRecyclerView() {
        binding.apply {
            listUsers.setHasFixedSize(true)
            listUsers.isNestedScrollingEnabled = false
            listUsers.adapter = usersAdapter
        }
    }

    private fun initListeners() {
        viewModel.users.observe(viewLifecycleOwner) {
            usersAdapter.submitList(it)
        }
    }

    private fun onViewLoaded() {
        viewModel.sendLoadTopUsersRequest()
    }
}