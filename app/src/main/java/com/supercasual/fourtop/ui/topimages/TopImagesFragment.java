package com.supercasual.fourtop.ui.topimages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.adapter.ImageAdapter;
import com.supercasual.fourtop.databinding.FragmentTopImagesBinding;
import com.supercasual.fourtop.model.Image;
import com.supercasual.fourtop.network.Network;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TopImagesFragment extends Fragment {

    private static final int LAYOUT = R.layout.fragment_top_images;

    private FragmentTopImagesBinding binding;

    private ImageAdapter imageAdapter;
    private List<Image> imagesList;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);

        imagesList = new ArrayList<>();
        binding.recyclerTopImages.setHasFixedSize(true);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Если адаптер существует и происходит поворот экрана, то адаптер обновляется.
        // TODO: Надо перенести imageList в ViewModel, чтобы не было этого костыля
        if (imageAdapter == null) {
            imagesList = Network.get(getContext()).topPhotosRequest(20, 0,
                    () -> {
                        binding.recyclerTopImages
                                .setLayoutManager(new LinearLayoutManager(getContext()));
                        imageAdapter = new ImageAdapter(getContext(), imagesList);
                        binding.recyclerTopImages.setAdapter(imageAdapter);
                    }
            );
        } else {
            imageAdapter.notifyDataSetChanged();
        }
    }
}
