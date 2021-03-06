package com.emikhalets.voteapp.view.screens

import android.os.Bundle
import android.view.View
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentTopUsersBinding
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.view.adapters.UsersAdapter
import com.emikhalets.voteapp.view.base.ContentFragment
import com.emikhalets.voteapp.viewmodel.TopUsersViewModel

class TopUsersFragment : ContentFragment<FragmentTopUsersBinding>(FragmentTopUsersBinding::inflate) {

    private lateinit var usersAdapter: UsersAdapter
    private lateinit var viewModel: TopUsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            setViewState(ViewState.LOADED)
            usersAdapter.submitList(it)
        }

        viewModel.error.observe(viewLifecycleOwner, EventObserver {
            setViewState(ViewState.LOADED)
            toast(R.string.app_toast_no_users_on_server)
        })
    }

    private fun onViewLoaded() {
        setViewState(ViewState.LOADING)
        viewModel.sendLoadTopUsersRequest()
    }

    private fun onUserClick(user: User) {
        toastLong("clicked on ${user.username}")
    }

    private fun setViewState(state: ViewState) {
        when (state) {
            ViewState.LOADING -> binding.progressBar.animShow()
            ViewState.LOADED -> binding.progressBar.animHide()
        }
    }
}