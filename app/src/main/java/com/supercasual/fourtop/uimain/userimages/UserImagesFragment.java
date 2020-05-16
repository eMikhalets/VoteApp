package com.supercasual.fourtop.uimain.userimages;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentUserImagesBinding;
import com.supercasual.fourtop.uimain.ImageAdapter;
import com.supercasual.fourtop.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

public class UserImagesFragment extends Fragment {

    private static final int IMAGE_REQUEST_CODE = 0;

    private FragmentUserImagesBinding binding;
    private UserImagesViewModel viewModel;
    private ImageAdapter adapter;

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

        viewModel.getApiGallery().observe(getViewLifecycleOwner(), images -> {
            adapter.setImages(images);
        });

        viewModel.getApiGalleryAdd().observe(getViewLifecycleOwner(), s -> {
            if (s.equals("OK")) {
                viewModel.galleryRequest();
            } else {
                Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getApiGalleryRemove().observe(getViewLifecycleOwner(), s -> {
            if (s.equals("OK")) {
                viewModel.galleryRequest();
            } else {
                Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.recyclerUserImages.setLayoutManager(layoutManager);
        adapter = new ImageAdapter();
        binding.recyclerUserImages.setAdapter(adapter);
        binding.recyclerUserImages.setHasFixedSize(true);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        int position = viewHolder.getAdapterPosition();
                        int id = viewModel.getApiGallery().getValue().get(position).getId();
                        viewModel.galleryRemoveRequest(
                                viewModel.getToken().getValue(),
                                String.valueOf(id));
                    }
                });
        itemTouchHelper.attachToRecyclerView(binding.recyclerUserImages);

        setHasOptionsMenu(true);
        setDataFromArguments();
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
                    FilePickerBuilder.getInstance()
                            .setMaxCount(1)
                            .pickPhoto(this, IMAGE_REQUEST_CODE);
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
                List<Uri> list = new ArrayList<>(
                        data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                Cursor cursor = requireContext().getContentResolver()
                        .query(list.get(0),
                                null,
                                null,
                                null,
                                null);
                cursor.moveToFirst();
                int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                String path = cursor.getString(index);
                File file = new File(path);
                viewModel.getFile().setValue(file);
                viewModel.galleryAddRequest(
                        viewModel.getToken().getValue(),
                        viewModel.getFile().getValue());
            }
        }
    }

    private void setDataFromArguments() {
        Bundle args = getArguments();
        if (args != null) {
            String token = args.getString(Constants.ARGS_TOKEN);
            if (token != null && !token.isEmpty()) {
                viewModel.getToken().setValue(token);
                viewModel.galleryRequest();
            } else {
                Toast.makeText(requireContext(), "Нет токена", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isStoragePermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    Constants.READ_EXTERNAL_PERMISSION, Constants.READ_EXTERNAL_REQUEST);
            return false;
        } else {
            return true;
        }
    }
}
