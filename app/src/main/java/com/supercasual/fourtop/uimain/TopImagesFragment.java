package com.supercasual.fourtop.uimain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentTopImagesBinding;
import com.supercasual.fourtop.network.pojo.AppResponse;
import com.supercasual.fourtop.network.pojo.DataImages;
import com.supercasual.fourtop.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TopImagesFragment extends Fragment {

    private FragmentTopImagesBinding binding;
    private TopImagesViewModel viewModel;
    private ImageAdapter adapter;

    private String token;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_top_images, container,
                false);
        viewModel = new ViewModelProvider(this).get(TopImagesViewModel.class);
        setArguments();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<DataImages> images = new ArrayList<>();
        binding.recyclerTopImages.setHasFixedSize(true);
        binding.recyclerTopImages.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ImageAdapter(getContext(), images);
        binding.recyclerTopImages.setAdapter(adapter);

        if (viewModel.getLiveData().getValue() == null) {
            loadImages();
        }
    }

    private void setArguments() {
        Bundle args = this.getArguments();

        if (args != null) {
            token = args.getString(Constants.ARGS_TOKEN);
        }
    }

    private void loadImages() {
        String count = String.valueOf(10);
        String offset = String.valueOf(0);

        LiveData<AppResponse> liveData = viewModel.topPhotos(token, count, offset);
        liveData.observe(getViewLifecycleOwner(), appResponse -> {
            adapter.setImages(appResponse.getDataImages());
        });
    }
}
