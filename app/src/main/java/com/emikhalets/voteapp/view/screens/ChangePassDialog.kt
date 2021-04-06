package com.emikhalets.voteapp.view.screens

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.DialogChangePassBinding
import com.emikhalets.voteapp.utils.*
import com.emikhalets.voteapp.viewmodel.ProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// TODO: With viewBinding delegate throws exception:
//  IllegalStateException: Fragment ChangePassDialog did not return a View from onCreateView()
//  or this was called before onCreateView().
class ChangePassDialog : DialogFragment() {

    private var _binding: DialogChangePassBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: ProfileViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewModel = injectViewModel(activity().viewModelFactory)
        _binding = DialogChangePassBinding.inflate(LayoutInflater.from(context))
        binding.btnApply.setOnClickListener { onApplyClick() }
        return MaterialAlertDialogBuilder(requireContext())
                .setView(binding.root)
                .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // TODO вынести проверки строк в валидатор, написать тесты
    private fun onApplyClick() {
        val newPass = binding.inputNew.text.toString()
        val newConf = binding.inputNewConf.text.toString()
        if (newPass.isNotEmpty() && newConf.isNotEmpty()) {
            if (newPass == newConf) {
                viewModel.sendUpdatePassRequest(newPass) { isSuccess, error ->
                    if (isSuccess) popBackStack()
                    else toastLong(error)
                }
            } else {
                toast(R.string.app_toast_pass_not_confirm)
            }
        } else {
            toast(R.string.app_toast_fill_fields)
        }
    }
}