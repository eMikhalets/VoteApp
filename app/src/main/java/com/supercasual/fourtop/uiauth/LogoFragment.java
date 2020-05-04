package com.supercasual.fourtop.uiauth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentLogoBinding;
import com.supercasual.fourtop.uimain.MainActivity;
import com.supercasual.fourtop.utils.Constants;

import org.jetbrains.annotations.NotNull;

public class LogoFragment extends Fragment {

    private FragmentLogoBinding binding;
    private LogoViewModel viewModel;
    private String token;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_logo, container,
                false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(LogoViewModel.class);
        viewIsLoaded();
    }

    public void viewIsLoaded() {
        loadUserToken();

        if (token != null && !token.isEmpty()) {
            viewModel.checkToken(token);
        } else {
            openLoginFragment();
        }

        viewModel.getLiveData().observe(getViewLifecycleOwner(), appResponse -> {
            String response = appResponse.getDataString();

            if (response.equals(token)) {
                openMainActivity(token);
            } else {
                openLoginFragment();
            }
        });
    }

    private void loadUserToken() {
        SharedPreferences sp = requireActivity().getSharedPreferences(
                Constants.SHARED_FILE, Context.MODE_PRIVATE);

        if (sp.contains(Constants.SHARED_TOKEN)) {
            token = sp.getString(Constants.SHARED_TOKEN, "");
        }
    }

    private void openMainActivity(String userToken) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(Constants.ARGS_TOKEN, userToken);
        startActivity(intent);
    }

    private void openLoginFragment() {
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_logoFragment_to_loginFragment);
    }
}
