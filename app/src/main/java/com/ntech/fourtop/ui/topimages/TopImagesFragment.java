package com.ntech.fourtop.ui.topimages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ntech.fourtop.adapters.ImageAdapter;
import com.ntech.fourtop.databinding.FragmentTopImagesBinding;
import com.ntech.fourtop.network.pojo.DataImage;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TopImagesFragment extends Fragment {

    private ImageAdapter adapter;
    private TopImagesViewModel viewModel;
    private FragmentTopImagesBinding binding;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTopImagesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TopImagesViewModel.class);
        viewModel.getImages().observe(getViewLifecycleOwner(), this::imagesObserver);
        viewModel.getThrowable().observe(getViewLifecycleOwner(), this::errorsObserver);
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), this::errorsObserver);

        adapter = new ImageAdapter();
        binding.recyclerTopImages.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerTopImages.setAdapter(adapter);
        binding.recyclerTopImages.setHasFixedSize(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void imagesObserver(List<DataImage> images) {
        adapter.setImages(images);
    }

    private void errorsObserver(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }
}
