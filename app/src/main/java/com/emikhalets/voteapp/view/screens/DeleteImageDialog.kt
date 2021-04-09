package com.emikhalets.voteapp.view.screens

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import com.emikhalets.voteapp.databinding.DialogDeleteImageBinding
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.view.base.BaseDialog
import com.emikhalets.voteapp.viewmodel.UserImagesViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DeleteImageDialog : BaseDialog() {

    private lateinit var viewModel: UserImagesViewModel
    private var _binding: DialogDeleteImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogDeleteImageBinding.inflate(LayoutInflater.from(context))
        binding.btnYes.setOnClickListener { onYesClick() }
        binding.btnNo.setOnClickListener { dismiss() }
        return MaterialAlertDialogBuilder(requireContext())
                .setView(binding.root)
                .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onYesClick() {
        arguments?.let {
            setViewState(ViewState.LOADING)
            val name = it.getString(ARGS_NAME) ?: ""
            val pos = it.getInt(ARGS_POS)
            viewModel.sendDeleteImageRequest(name, pos) { success, error ->
                setViewState(ViewState.LOADED)
                if (success) popBackStack()
                else toastLong(error)
            }
        }
    }

    private fun setViewState(state: ViewState) {
        when (state) {
            ViewState.LOADING -> binding.progressBar.animShow()
            ViewState.LOADED -> binding.progressBar.animHide()
        }
    }
}