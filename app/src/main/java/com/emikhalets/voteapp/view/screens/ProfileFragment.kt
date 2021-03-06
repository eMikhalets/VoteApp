package com.emikhalets.voteapp.view.screens

import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.findNavController
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.FragmentProfileBinding
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.view.TakeImageContract
import com.emikhalets.voteapp.view.base.ContentFragment
import com.emikhalets.voteapp.viewmodel.ProfileViewModel

class ProfileFragment : ContentFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var takeImageResult: ActivityResultLauncher<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        takeImageResult = registerForActivityResult(TakeImageContract(activity())) {
            onTakeImageResult(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity().title = getString(R.string.profile_title)
        if (savedInstanceState == null) onViewLoaded()
        if (userPhoto().isNotEmpty() && userPhoto() != "null") ViewCompat.setTransitionName(binding.image, userPhoto())
        initListeners()
    }

    private fun initListeners() {
        viewModel.user.observe(viewLifecycleOwner, {
            setViewState(ViewState.LOADED)
            setData(it)
        })

        viewModel.passwordState.observe(viewLifecycleOwner, {
            setViewState(ViewState.LOADED)
        })

        viewModel.logoutState.observe(viewLifecycleOwner, {
            setViewState(ViewState.LOADED)
            findNavController().navigate(R.id.authLoginFragment)
        })

        viewModel.error.observe(viewLifecycleOwner, EventObserver { message ->
            setViewState(ViewState.LOADED)
            toastLong(message)
        })

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
        setViewState(ViewState.LOADING)
        viewModel.sendLoadUserDataRequest()
    }

    private fun onPhotoClick() {
        if (userPhoto().isNotEmpty() && userPhoto() != "null") {
            navigate(ProfileFragmentDirections.actionProfileToImage(userPhoto()),
                    binding.image to userPhoto())
        }
    }

    private fun onChangePhotoClick() {
        takeImageResult.launch(500)
    }

    private fun onTakeImageResult(uri: Uri?) {
        setViewState(ViewState.LOADING)
        viewModel.sendUpdateUserPhotoRequest(uri.toString())
    }

    private fun onChangeUsernameClick() {
        navigate(ProfileFragmentDirections.actionProfileToChangeName())
    }

    private fun onChangePassClick() {
        navigate(ProfileFragmentDirections.actionProfileToChangePass())
    }

    private fun onLogoutClick() {
        setViewState(ViewState.LOADING)
        viewModel.sendLogOutRequest()
    }

    private fun setViewState(state: ViewState) {
        when (state) {
            ViewState.LOADING -> binding.progressBar.animShow()
            ViewState.LOADED -> binding.progressBar.animHide()
        }
    }
}