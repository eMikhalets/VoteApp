package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentTopUsersBinding
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.utils.activity
import com.emikhalets.voteapp.utils.injectViewModel
import com.emikhalets.voteapp.utils.toastLong
import com.emikhalets.voteapp.view.adapters.UsersAdapter
import com.emikhalets.voteapp.view.base.ContentFragment
import com.emikhalets.voteapp.viewmodel.TopUsersViewModel

class TopUsersFragment : ContentFragment<FragmentTopUsersBinding>(FragmentTopUsersBinding::inflate) {

    private lateinit var usersAdapter: UsersAdapter
    lateinit var viewModel: TopUsersViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = injectViewModel(activity().viewModelFactory)
        activity().title = getString(R.string.top_users_title)
        initRecyclerView()
        initListeners()
        if (savedInstanceState == null) onViewLoaded()
    }

    private fun initRecyclerView() {
        usersAdapter = UsersAdapter { onUserClick(it) }
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

    private fun onUserClick(user: User) {
        toastLong("clicked on ${user.username}")
    }
}