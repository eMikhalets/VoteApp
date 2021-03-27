package com.emikhalets.voteapp.view.topimages

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.emikhalets.voteapp.adapters.ImageAdapter
import com.emikhalets.voteapp.databinding.FragmentTopImagesBinding
import com.emikhalets.voteapp.network.pojo.DataImage
import com.emikhalets.voteapp.utils.Const

class TopImagesFragment : Fragment() {
    private var adapter: ImageAdapter? = null
    private var viewModel: TopImagesViewModel? = null
    private var binding: FragmentTopImagesBinding? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentTopImagesBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(TopImagesViewModel::class.java)
        viewModel!!.images.observe(viewLifecycleOwner, { images: List<DataImage?>? -> imagesObserver(images) })
        viewModel!!.throwable.observe(viewLifecycleOwner, { error: String? -> errorsObserver(error) })
        viewModel!!.errorMessage.observe(viewLifecycleOwner, { error: String? -> errorsObserver(error) })
        adapter = ImageAdapter()
        binding!!.recyclerTopImages.layoutManager = LinearLayoutManager(requireContext())
        binding!!.recyclerTopImages.adapter = adapter
        binding!!.recyclerTopImages.setHasFixedSize(true)
        if (savedInstanceState == null) {
            loadUserToken()
            viewModel!!.topPhotosRequest()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun imagesObserver(images: List<DataImage?>?) {
        adapter!!.setImages(images)
    }

    private fun errorsObserver(error: String?) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    private fun loadUserToken() {
        val sp = requireActivity().getSharedPreferences(
                Const.SHARED_FILE, Context.MODE_PRIVATE)
        viewModel!!.setUserToken(sp.getString(Const.SHARED_TOKEN, ""))
    }
}