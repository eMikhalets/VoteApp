package com.emikhalets.voteapp.view.top_users

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentTopUsersBinding
import com.emikhalets.voteapp.test.createMockUsers
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.view.adapters.UsersAdapter
import com.emikhalets.voteapp.view.base.SecondaryFragment

class TopUsersFragment : SecondaryFragment(R.layout.fragment_top_users) {

    private val binding: FragmentTopUsersBinding by viewBinding()
    private lateinit var usersAdapter: UsersAdapter
    private lateinit var llm: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ACTIVITY.title = getString(R.string.top_users_title)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        usersAdapter = UsersAdapter()
        llm = LinearLayoutManager(this.context)
        binding.apply {
            listUsers.layoutManager = llm
            listUsers.setHasFixedSize(true)
            listUsers.isNestedScrollingEnabled = false
            listUsers.adapter = usersAdapter
        }
        usersAdapter.updateList(createMockUsers().sortedByDescending { it.rating })
    }
}