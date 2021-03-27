package com.emikhalets.voteapp.view.userimages

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.adapters.ImageAdapter
import com.emikhalets.voteapp.databinding.FragmentUserImagesBinding
import com.emikhalets.voteapp.network.pojo.DataImage
import com.emikhalets.voteapp.utils.Const

class UserImagesFragment : Fragment() {
    private var adapter: ImageAdapter? = null
    private var viewModel: UserImagesViewModel? = null
    private var binding: FragmentUserImagesBinding? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentUserImagesBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserImagesViewModel::class.java)
        viewModel!!.images.observe(viewLifecycleOwner, { images: List<DataImage?>? -> imagesObserver(images) })
        viewModel!!.throwable.observe(viewLifecycleOwner, { error: String? -> errorObserver(error) })
        viewModel!!.errorMessage.observe(viewLifecycleOwner, { error: String? -> errorObserver(error) })
        adapter = ImageAdapter()
        binding!!.recyclerUserImages.layoutManager = LinearLayoutManager(requireContext())
        binding!!.recyclerUserImages.adapter = adapter
        binding!!.recyclerUserImages.setHasFixedSize(true)
        val itemTouchHelper = itemTouchHelper
        itemTouchHelper.attachToRecyclerView(binding!!.recyclerUserImages)
        setHasOptionsMenu(true)
        if (savedInstanceState == null) {
            loadUserToken()
            viewModel!!.galleryRequest()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_user_images, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_user_images_add -> return if (isStoragePermission) {
                Toast.makeText(context, "Not implemented...", Toast.LENGTH_SHORT).show()
                // TODO: implement adding image
                true
            } else {
                Toast.makeText(context, "Permission error", Toast.LENGTH_SHORT).show()
                false
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
//                List<Uri> list = new ArrayList<>(
//                        data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
//                Cursor cursor = requireContext().getContentResolver()
//                        .query(list.get(0),
//                                null,
//                                null,
//                                null,
//                                null);
//                cursor.moveToFirst();
//                int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//                String path = cursor.getString(index);
//                File file = new File(path);
//                viewModel.getFile().setValue(file);
//                viewModel.galleryAddRequest(
//                        viewModel.getToken().getValue(),
//                        viewModel.getFile().getValue());
            }
        }
    }

    private fun imagesObserver(images: List<DataImage?>?) {
        adapter!!.setImages(images)
    }

    private fun errorObserver(error: String?) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    private val itemTouchHelper: ItemTouchHelper
        private get() = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder,
                                  direction: Int) {
                viewModel!!.galleryRemoveRequest(viewHolder.adapterPosition)
            }
        })
    private val isStoragePermission: Boolean
        private get() = if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    Const.READ_EXTERNAL_PERMISSION, Const.READ_EXTERNAL_REQUEST)
            false
        } else {
            true
        }

    private fun loadUserToken() {
        val sp = requireActivity().getSharedPreferences(
                Const.SHARED_FILE, Context.MODE_PRIVATE)
        viewModel!!.setUserToken(sp.getString(Const.SHARED_TOKEN, ""))
    }

    companion object {
        private const val IMAGE_REQUEST_CODE = 0
    }
}