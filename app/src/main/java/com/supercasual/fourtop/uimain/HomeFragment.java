package com.supercasual.fourtop.uimain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentHomeBinding;
import com.supercasual.fourtop.utils.Constants;
import com.supercasual.fourtop.viewmodel.HomeViewModel;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        viewModel.getData().observe(this.getViewLifecycleOwner(), strings -> {
            binding.textHomeHeader.setText(strings.get(0));
            binding.textHomeContent.setText(strings.get(1));
        });

        return binding.getRoot();
    }

    private void setNabHeader() {
        String token = getArguments().getString(Constants.ARGS_TOKEN);
    }
}