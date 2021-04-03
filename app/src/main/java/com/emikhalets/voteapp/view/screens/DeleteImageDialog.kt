package com.emikhalets.voteapp.view.screens

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.emikhalets.voteapp.databinding.DialogDeleteImageBinding
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.viewmodel.HomeViewModel
import com.emikhalets.voteapp.viewmodel.UserImagesViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

// TODO: With viewBinding delegate throws exception:
//  IllegalStateException: Fragment DeleteImageDialog did not return a View from onCreateView()
//  or this was called before onCreateView().
class DeleteImageDialog : DialogFragment() {

    private var _binding: DialogDeleteImageBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: UserImagesViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewModel = injectViewModel(ACTIVITY.viewModelFactory)
        _binding = DialogDeleteImageBinding.inflate(LayoutInflater.from(context))
        binding.btnYes.setOnClickListener { onYesClick() }
        binding.btnNo.setOnClickListener { dismiss() }
        return MaterialAlertDialogBuilder(ACTIVITY)
                .setView(binding.root)
                .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onYesClick() {
        arguments?.let {
            val name = it.getString(ARGS_NAME) ?: ""
            val pos = it.getInt(ARGS_POS)
            viewModel.sendDeleteImageRequest(name, pos) {
                lifecycleScope.launch {
                    popBackStack()
                }
            }
        }
    }
}