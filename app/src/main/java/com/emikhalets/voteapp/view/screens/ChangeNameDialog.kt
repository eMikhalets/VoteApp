package com.emikhalets.voteapp.view.screens

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.DialogChangeNameBinding
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.utils.popBackStack
import com.emikhalets.voteapp.utils.toast
import com.emikhalets.voteapp.viewmodel.ProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

/**
 * With viewBinding delegate throws exception:
 * IllegalStateException: Fragment ChangeNameDialog did not return a View from onCreateView()
 * or this was called before onCreateView().
 */
class ChangeNameDialog : DialogFragment() {

    private var _binding: DialogChangeNameBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogChangeNameBinding.inflate(LayoutInflater.from(context))
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
        val name = binding.inputName.text.toString()
        if (name.isNotEmpty()) viewModel.sendUpdateUsernameRequest(name) {
            lifecycleScope.launch {
                popBackStack()
            }
        }
        else toast(getString(R.string.app_toast_fill_fields))
    }
}