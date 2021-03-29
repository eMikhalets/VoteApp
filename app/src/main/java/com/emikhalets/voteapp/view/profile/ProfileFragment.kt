package com.emikhalets.voteapp.view.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentProfileBinding
import com.emikhalets.voteapp.test.createMockUser
import com.emikhalets.voteapp.test.loadMock
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.utils.navigate
import com.emikhalets.voteapp.view.base.SecondaryFragment
import com.emikhalets.voteapp.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

class ProfileFragment : SecondaryFragment(R.layout.fragment_profile) {

    private val binding: FragmentProfileBinding by viewBinding()
    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ACTIVITY.title = getString(R.string.profile_title)
        setUserData()
        initListeners()
    }

    private fun setUserData() {
        val user = createMockUser()
        binding.apply {
            image.loadMock(user.photo.toInt())
            textUsername.text = user.username
            textRating.text = getString(R.string.profile_text_rating, user.rating)
        }
    }

    private fun initListeners() {
        binding.apply {
            btnChangePass.setOnClickListener { onChangePassClick() }
            btnLogout.setOnClickListener { onLogoutClick() }
        }
    }

    private fun onChangePassClick() {
        navigate(R.id.action_profile_to_changePass)
    }

    private fun onLogoutClick() {
        viewModel.sendLogOutRequest {
            lifecycleScope.launch {
                navigate(R.id.authLoginFragment)
            }
        }
    }
}