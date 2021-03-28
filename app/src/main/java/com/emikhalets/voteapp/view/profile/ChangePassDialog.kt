package com.emikhalets.voteapp.view.profile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.DialogChangePassBinding
import com.emikhalets.voteapp.utils.ACTIVITY
import com.emikhalets.voteapp.utils.popBackStack
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ChangePassDialog : DialogFragment() {

    private var _binding: DialogChangePassBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogChangePassBinding.inflate(LayoutInflater.from(context))
        return MaterialAlertDialogBuilder(ACTIVITY)
                .setView(binding.root)
                .setPositiveButton(getString(R.string.profile_btn_apply)) { _, _ ->
                    changePassword()
                }
                .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun changePassword() {
        popBackStack()
    }
}