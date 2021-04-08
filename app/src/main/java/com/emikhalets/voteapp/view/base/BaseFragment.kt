package com.emikhalets.voteapp.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.emikhalets.voteapp.di.viewmodel.ViewModelFactory
import com.emikhalets.voteapp.utils.Inflate
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<T : ViewBinding>(private val inflate: Inflate<T>) : DaggerFragment() {

    private var _binding: T? = null
    val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}