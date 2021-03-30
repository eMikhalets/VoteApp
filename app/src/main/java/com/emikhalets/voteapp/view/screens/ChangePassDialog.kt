package com.emikhalets.voteapp.view.screens

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.DialogChangePassBinding
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.utils.popBackStack
import com.emikhalets.voteapp.utils.toast
import com.emikhalets.voteapp.viewmodel.ProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class ChangePassDialog : DialogFragment() {

    private var _binding: DialogChangePassBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogChangePassBinding.inflate(LayoutInflater.from(context))
        binding.btnApply.setOnClickListener { onApplyClick() }
        return MaterialAlertDialogBuilder(ACTIVITY)
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
        if (newPass.isNotEmpty() && newConf.isNotEmpty()) {
            if (newPass == newConf) {
                viewModel.sendUpdatePassRequest(newPass) {
                    lifecycleScope.launch {
                        popBackStack()
                    }
                }
            } else {
                toast(getString(R.string.app_toast_pass_not_confirm))
            }
        } else {
            toast(getString(R.string.app_toast_fill_fields))
        }
    }
}