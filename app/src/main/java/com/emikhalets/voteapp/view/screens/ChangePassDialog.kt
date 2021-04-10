package com.emikhalets.voteapp.view.screens

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.DialogChangePassBinding
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.view.base.BaseDialog
import com.emikhalets.voteapp.viewmodel.ProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ChangePassDialog : BaseDialog() {

    private lateinit var viewModel: ProfileViewModel
    private var _binding: DialogChangePassBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogChangePassBinding.inflate(LayoutInflater.from(context))

        viewModel.passwordState.observe(viewLifecycleOwner, { popBackStack() })

        viewModel.error.observe(viewLifecycleOwner, EventObserver { message ->
            setViewState(ViewState.LOADED)
            toastLong(message)
        })

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
        val newPass = binding.inputNew.text.toString()
        val newConf = binding.inputNewConf.text.toString()
        validateChangePass(newPass, newConf) {
            when (it) {
                ChangePassToast.EMPTY_FIELDS -> toast(R.string.app_toast_fill_fields)
                ChangePassToast.INVALID_PASS -> toast(R.string.app_toast_invalid_pass)
                ChangePassToast.PASS_MISMATCH -> toast(R.string.app_toast_pass_not_confirm)
                ChangePassToast.SUCCESS -> {
                    setViewState(ViewState.LOADING)
                    viewModel.sendUpdatePassRequest(newPass)
                }
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