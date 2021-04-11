package com.emikhalets.voteapp.view.screens

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.DialogResetPassBinding
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.view.base.BaseDialog
import com.emikhalets.voteapp.viewmodel.LoginViewModel

class ResetPassDialog : BaseDialog() {

    private lateinit var viewModel: LoginViewModel
    private var _binding: DialogResetPassBinding? = null
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
        _binding = DialogResetPassBinding.inflate(LayoutInflater.from(context))

        viewModel.passResetState.observe(activity(), {
            toastLong(R.string.app_toast_reset_pass_email_send)
            popBackStack()
        })

        viewModel.error.observe(activity(), EventObserver { message ->
            setViewState(ViewState.LOADED)
            toastLong(message)
        })

        binding.btnReset.setOnClickListener { onResetClick() }

        return AlertDialog.Builder(requireContext())
                .setView(binding.root)
                .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onResetClick() {
        hideKeyboard()
        val email = binding.inputEmail.text.toString()
        validateResetPass(email) {
            when (it) {
                ResetPassToast.EMPTY_FIELDS -> toast(R.string.app_toast_fill_fields)
                ResetPassToast.INVALID_EMAIL -> toast(R.string.app_toast_invalid_email)
                ResetPassToast.SUCCESS -> {
                    setViewState(ViewState.LOADING)
                    viewModel.sendResetPassRequest(email)
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