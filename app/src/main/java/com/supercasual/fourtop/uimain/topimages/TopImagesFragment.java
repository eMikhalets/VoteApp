package com.supercasual.fourtop.uimain.topimages;

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

import com.supercasual.fourtop.databinding.FragmentTopImagesBinding;
import com.supercasual.fourtop.uimain.ImageAdapter;
import com.supercasual.fourtop.utils.Constants;

import org.jetbrains.annotations.NotNull;

public class TopImagesFragment extends Fragment {

    private FragmentTopImagesBinding binding;
    private TopImagesViewModel viewModel;
    private ImageAdapter adapter;


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

        viewModel.getApiTopImages().observe(getViewLifecycleOwner(), images -> {
            adapter.setImages(images);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.recyclerTopImages.setLayoutManager(layoutManager);
        adapter = new ImageAdapter();
        binding.recyclerTopImages.setAdapter(adapter);
        binding.recyclerTopImages.setHasFixedSize(true);

        setArguments();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setArguments() {
        Bundle args = this.getArguments();

        if (args != null) {
            String token = args.getString(Constants.ARGS_TOKEN);

            if (token != null && !token.isEmpty()) {
                viewModel.getToken().setValue(token);
                viewModel.topPhotosRequest();
            } else {
                Toast.makeText(requireContext(), "Нет токена", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
