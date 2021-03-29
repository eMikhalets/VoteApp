package com.emikhalets.voteapp.view.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentProfileBinding
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.utils.loadImage
import com.emikhalets.voteapp.utils.navigate
import com.emikhalets.voteapp.view.TakeImageContract
import com.emikhalets.voteapp.view.base.SecondaryFragment
import com.emikhalets.voteapp.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

class ProfileFragment : SecondaryFragment(R.layout.fragment_profile) {

    private val binding: FragmentProfileBinding by viewBinding()
    private val viewModel: ProfileViewModel by viewModels()

    private val takeImageResult = registerForActivityResult(TakeImageContract()) {
        viewModel.sendUpdateImageRequest(it) { url ->
            lifecycleScope.launch {
                binding.image.loadImage(url)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ACTIVITY.title = getString(R.string.profile_title)
        initListeners()
        onViewLoaded()
    }

    private fun onViewLoaded() {
        viewModel.sendUserDataRequest { data ->
            lifecycleScope.launch {
                binding.apply {
                    image.loadImage(data.photo)
                    textUsername.text = data.username
                    textRating.text = getString(R.string.profile_text_rating, data.rating)
                }
            }
        }
    }

    private fun initListeners() {
        binding.apply {
            image.setOnClickListener { onPhotoClick() }
            btnChangePass.setOnClickListener { onChangePassClick() }
            btnLogout.setOnClickListener { onLogoutClick() }
        }
    }

    private fun onPhotoClick() {
        takeImageResult.launch(100)
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