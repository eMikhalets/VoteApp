package com.emikhalets.voteapp.view.screens

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.DialogChangeNameBinding
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.view.base.BaseDialog
import com.emikhalets.voteapp.viewmodel.ProfileViewModel

class ChangeNameDialog : BaseDialog() {

    private lateinit var viewModel: ProfileViewModel
    private var _binding: DialogChangeNameBinding? = null
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
        _binding = DialogChangeNameBinding.inflate(LayoutInflater.from(context))

        viewModel.usernameState.observe(activity(), { popBackStack() })

        viewModel.error.observe(activity(), EventObserver { message ->
            setViewState(ViewState.LOADED)
            toastLong(message)
        })

        binding.btnApply.setOnClickListener { onApplyClick() }

        return AlertDialog.Builder(requireContext())
                .setView(binding.root)
                .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onApplyClick() {
        val name = binding.inputName.text.toString()
        validateChangeName(name) {
            when (it) {
                ChangeNameToast.EMPTY_FIELDS -> toast(R.string.app_toast_fill_fields)
                ChangeNameToast.SUCCESS -> {
                    setViewState(ViewState.LOADING)
                    viewModel.sendUpdateUsernameRequest(name)
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