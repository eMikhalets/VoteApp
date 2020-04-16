package com.supercasual.fourtop.uimain;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.adapter.ImageAdapter;
import com.supercasual.fourtop.databinding.FragmentTopImagesBinding;
import com.supercasual.fourtop.network.pojo.ImagesData;
import com.supercasual.fourtop.utils.Constants;
import com.supercasual.fourtop.viewmodel.TopImagesViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TopImagesFragment extends Fragment {

    private FragmentTopImagesBinding binding;
    private TopImagesViewModel viewModel;
    private ImageAdapter imageAdapter;
    private List<ImagesData> imagesList;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_top_images, container,
                false);
        viewModel = new ViewModelProvider(this).get(TopImagesViewModel.class);

        imagesList = new ArrayList<>();
        binding.recyclerTopImages.setHasFixedSize(true);
        binding.recyclerTopImages.setLayoutManager(new LinearLayoutManager(getContext()));
        imageAdapter = new ImageAdapter(imagesList);
        binding.recyclerTopImages.setAdapter(imageAdapter);

        loadImages();

        return binding.getRoot();
    }

    private void loadImages() {
        String token = getArguments().getString(Constants.ARGS_TOKEN);
        viewModel.sendTopPhotosRequest(token, imageAdapter);

        LiveData<List<ImagesData>> liveData = viewModel.getLiveData();
        liveData.observe(getViewLifecycleOwner(), new Observer<List<ImagesData>>() {
            @Override
            public void onChanged(List<ImagesData> imagesData) {
                imageAdapter.setImages(imagesData);
            }
        });
    }
}
