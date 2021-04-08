package com.emikhalets.voteapp.view.screens

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.DialogChangeNameBinding
import com.emikhalets.voteapp.utils.injectViewModel
import com.emikhalets.voteapp.utils.popBackStack
import com.emikhalets.voteapp.utils.toast
import com.emikhalets.voteapp.utils.toastLong
import com.emikhalets.voteapp.view.base.BaseDialog
import com.emikhalets.voteapp.viewmodel.ProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ChangeNameDialog : BaseDialog() {

    private lateinit var viewModel: ProfileViewModel
    private var _binding: DialogChangeNameBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogChangeNameBinding.inflate(LayoutInflater.from(context))
        binding.btnApply.setOnClickListener { onApplyClick() }
        return MaterialAlertDialogBuilder(requireContext())
                .setView(binding.root)
                .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onApplyClick() {
        val name = binding.inputName.text.toString()
        if (name.isNotEmpty()) {
            viewModel.sendUpdateUsernameRequest(name) { isSuccess, error ->
                if (isSuccess) popBackStack()
                else toastLong(error)
            }
        } else {
            toast(R.string.app_toast_fill_fields)
        }
    }
}