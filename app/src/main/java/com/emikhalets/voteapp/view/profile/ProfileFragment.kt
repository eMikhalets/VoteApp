package com.emikhalets.voteapp.view.profile

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentProfileBinding
import com.emikhalets.voteapp.test.createMockUser
import com.emikhalets.voteapp.test.loadMock
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.utils.navigate
import com.emikhalets.voteapp.view.base.SecondaryFragment

class ProfileFragment : SecondaryFragment(R.layout.fragment_profile) {

    private val binding: FragmentProfileBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ACTIVITY.title = getString(R.string.profile_title)
        setData()
        initClickListeners()
    }

    private fun setData() {
        val user = createMockUser()
        binding.apply {
            image.loadMock(user.photo.toInt())
            textUsername.text = user.username
            textRating.text = getString(R.string.profile_text_rating, user.rating)
        }
    }

    private fun initClickListeners() {
        binding.apply {
            btnChangePass.setOnClickListener { changePassword() }
            btnLogout.setOnClickListener { logout() }
        }
    }

    private fun changePassword() {
        navigate(R.id.action_profile_to_changePass)
    }

    private fun logout() {
        navigate(R.id.authLoginFragment)
    }
}