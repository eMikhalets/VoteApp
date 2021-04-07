package com.emikhalets.voteapp.view.screens

import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import by.kirich1409.viewbindingdelegate.viewBinding
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentProfileBinding
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.view.TakeImageContract
import com.emikhalets.voteapp.view.base.WithDrawerFragment
import com.emikhalets.voteapp.viewmodel.ProfileViewModel

class ProfileFragment : WithDrawerFragment(R.layout.fragment_profile) {

    private val binding: FragmentProfileBinding by viewBinding()
    private val viewModel by viewModels<ProfileViewModel>({ activity() }) { activity().viewModelFactory }

    private lateinit var takeImageResult: ActivityResultLauncher<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        takeImageResult = registerForActivityResult(TakeImageContract(activity())) {
            onTakeImageResult(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedElementEnterTransition =
                TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        activity().title = getString(R.string.profile_title)
        initListeners()
        if (savedInstanceState == null) onViewLoaded()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeUserListener()
    }

    private fun initListeners() {
        viewModel.user.observe(viewLifecycleOwner) {
            setData(it)
        }
        binding.apply {
            image.setOnClickListener { onPhotoClick() }
            btnChangePhoto.setOnClickListener { onChangePhotoClick() }
            btnChangeName.setOnClickListener { onChangeUsernameClick() }
            btnChangePass.setOnClickListener { onChangePassClick() }
            btnLogout.setOnClickListener { onLogoutClick() }
        }
    }

    private fun setData(user: User) {
        binding.apply {
            image.loadImage(user.photo, R.drawable.placeholder_user)
            textUsername.text = user.username
            textRating.text = getString(R.string.profile_text_rating, user.rating)
        }
    }

    private fun onViewLoaded() {
        viewModel.sendLoadUserDataRequest()
    }

    private fun onPhotoClick() {
        if (userPhoto().isNotEmpty() && userPhoto() != "null") {
            val args = bundleOf(ARGS_PHOTO to userPhoto())
            val extras = FragmentNavigatorExtras(binding.image to getString(R.string.app_transition_name_image_zoom))
            navigate(R.id.action_profile_to_image, args, extras = extras)
        }
    }

    private fun onChangePhotoClick() {
        takeImageResult.launch(500)
    }

    private fun onTakeImageResult(uri: Uri?) {
        viewModel.sendUpdateUserPhotoRequest(uri) { isSuccess, error ->
            if (isSuccess) binding.image.loadImage(userPhoto(), R.drawable.placeholder_user)
            else toastLong(error)
        }
    }

    private fun onChangeUsernameClick() {
        navigate(R.id.action_profile_to_changeName)
    }

    private fun onChangePassClick() {
        navigate(R.id.action_profile_to_changePass)
    }

    private fun onLogoutClick() {
        viewModel.sendLogOutRequest {
            navigate(R.id.authLoginFragment)
        }
    }
}