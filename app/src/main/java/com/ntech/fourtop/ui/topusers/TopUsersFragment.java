package com.ntech.fourtop.ui.topusers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ntech.fourtop.databinding.FragmentTopUsersBinding;

import org.jetbrains.annotations.NotNull;

public class TopUsersFragment extends Fragment {

    private FragmentTopUsersBinding binding;
    private TopUsersViewModel viewModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTopUsersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TopUsersViewModel.class);

        viewModel.getApiTopUsers().observe(getViewLifecycleOwner(), strings -> {
            // TODO: update users adapter
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
