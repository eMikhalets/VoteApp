package com.supercasual.fourtop.uimain;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentUserImagesBinding;
import com.supercasual.fourtop.network.pojo.AppResponse;
import com.supercasual.fourtop.network.pojo.DataImages;
import com.supercasual.fourtop.utils.Constants;
import com.supercasual.fourtop.utils.FilePath;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UserImagesFragment extends Fragment {

    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int MY_READ_EXTERNAL_STORAGE = 1;

    private FragmentUserImagesBinding binding;
    private UserImagesViewModel viewModel;
    private ImageAdapter adapter;

    private String token;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_images, container,
                false);
        viewModel = new ViewModelProvider(this).get(UserImagesViewModel.class);
        setArguments();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        List<DataImages> images = new ArrayList<>();
        binding.recyclerUserImages.setHasFixedSize(true);
        binding.recyclerUserImages.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ImageAdapter(getContext(), images);
        binding.recyclerUserImages.setAdapter(adapter);

        if (viewModel.getLiveData().getValue() == null) {
            loadImages();
        }
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
                if (getStoragePermission()) {
                    performImageSearch();
                } else {
                    Toast.makeText(getContext(), "Permission error", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                FilePath filePath = new FilePath();
                String imagePath = filePath.getUriRealPath(getContext(), uri);
                Log.d("TAG", "imagePath: " + imagePath);
            }
        }
    }

    private void setArguments() {
        Bundle args = getArguments();

        if (args != null) {
            token = args.getString(Constants.ARGS_TOKEN);
        }
    }

    private void loadImages() {
        String count = String.valueOf(10);
        String offset = String.valueOf(0);

        LiveData<AppResponse> liveData = viewModel.gallery(token, count, offset);
        liveData.observe(getViewLifecycleOwner(), appResponse -> {
            adapter.setImages(appResponse.getDataImages());
        });
    }

    private boolean getStoragePermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_READ_EXTERNAL_STORAGE);
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    private void performImageSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }
}
