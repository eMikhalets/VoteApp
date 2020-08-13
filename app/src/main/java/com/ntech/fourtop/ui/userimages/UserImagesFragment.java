package com.ntech.fourtop.ui.userimages;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ntech.fourtop.R;
import com.ntech.fourtop.adapters.ImageAdapter;
import com.ntech.fourtop.databinding.FragmentUserImagesBinding;
import com.ntech.fourtop.network.pojo.DataImage;
import com.ntech.fourtop.utils.Const;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserImagesFragment extends Fragment {

    private static final int IMAGE_REQUEST_CODE = 0;

    private ImageAdapter adapter;
    private UserImagesViewModel viewModel;
    private FragmentUserImagesBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserImagesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(UserImagesViewModel.class);
        viewModel.getImages().observe(getViewLifecycleOwner(), this::imagesObserver);
        viewModel.getThrowable().observe(getViewLifecycleOwner(), this::errorObserver);
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), this::errorObserver);

        adapter = new ImageAdapter();
        binding.recyclerUserImages.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerUserImages.setAdapter(adapter);
        binding.recyclerUserImages.setHasFixedSize(true);

        ItemTouchHelper itemTouchHelper = getItemTouchHelper();
        itemTouchHelper.attachToRecyclerView(binding.recyclerUserImages);

        setHasOptionsMenu(true);
        if (savedInstanceState == null) {
            loadUserToken();
            viewModel.galleryRequest();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_user_images, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_user_images_add:
                if (isStoragePermission()) {
                    Toast.makeText(getContext(), "Not implemented...", Toast.LENGTH_SHORT).show();
                    // TODO: implement adding image
                    return true;
                } else {
                    Toast.makeText(getContext(), "Permission error", Toast.LENGTH_SHORT).show();
                    return false;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    private void imagesObserver(List<DataImage> images) {
        adapter.setImages(images);
    }

    private void errorObserver(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }

    private ItemTouchHelper getItemTouchHelper() {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                                 int direction) {
                viewModel.galleryRemoveRequest(viewHolder.getAdapterPosition());
            }
        });
    }

    private boolean isStoragePermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    Const.READ_EXTERNAL_PERMISSION, Const.READ_EXTERNAL_REQUEST);
            return false;
        } else {
            return true;
        }
    }

    private void loadUserToken() {
        SharedPreferences sp = requireActivity().getSharedPreferences(
                Const.SHARED_FILE, Context.MODE_PRIVATE);
        viewModel.setUserToken(sp.getString(Const.SHARED_TOKEN, ""));
    }
}
