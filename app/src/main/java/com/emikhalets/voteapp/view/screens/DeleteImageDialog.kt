package com.emikhalets.voteapp.view.screens

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.DialogDeleteImageBinding
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.view.base.BaseDialog
import com.emikhalets.voteapp.viewmodel.UserImagesViewModel

class DeleteImageDialog : BaseDialog() {

    private lateinit var viewModel: UserImagesViewModel
    private var _binding: DialogDeleteImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnim
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogDeleteImageBinding.inflate(LayoutInflater.from(context))
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnim

        viewModel.deleteState.observe(activity(), { popBackStack() })

        viewModel.error.observe(activity(), EventObserver { message ->
            setViewState(ViewState.LOADED)
            toastLong(message)
        })

        binding.btnYes.setOnClickListener { onYesClick() }

        binding.btnNo.setOnClickListener { dismiss() }

        return AlertDialog.Builder(requireContext())
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
            viewModel.sendDeleteImageRequest(name)
        }
    }

    private fun setViewState(state: ViewState) {
        when (state) {
            ViewState.LOADING -> binding.progressBar.animShow()
            ViewState.LOADED -> binding.progressBar.animHide()
        }
    }
}