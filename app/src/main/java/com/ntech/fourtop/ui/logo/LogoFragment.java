package com.ntech.fourtop.ui.logo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.ntech.fourtop.R;
import com.ntech.fourtop.databinding.FragmentLogoBinding;
import com.ntech.fourtop.utils.Const;

import org.jetbrains.annotations.NotNull;

public class LogoFragment extends Fragment {

    private LogoViewModel viewModel;
    private FragmentLogoBinding binding;
    private String userToken;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,                             Bundle savedInstanceState) {
        binding = FragmentLogoBinding.inflate(inflater, container, false);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(LogoViewModel.class);
        viewModel.getThrowable().observe(getViewLifecycleOwner(), this::throwableObserver);
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), this::errorMessageObserver);
        viewModel.getLiveDataResponse().observe(getViewLifecycleOwner(), this::responseObserver);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (userToken == null) loadUserToken();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void throwableObserver(String throwable) {
        binding.textError.setText(throwable);
    }

    private void errorMessageObserver(String throwable) {
        navigateToLogin();
    }

    private void responseObserver(int status) {
        navigateToHome();
    }

    // Get user token from SharedPreferences.
    // Send token request or navigate to login view
    private void loadUserToken() {
        SharedPreferences sp = requireActivity().getSharedPreferences(
                Const.SHARED_FILE, Context.MODE_PRIVATE);
        userToken = sp.getString(Const.SHARED_TOKEN, "");

        if (userToken != null && !userToken.isEmpty()) {
            viewModel.tokenRequest(userToken);
        } else {
            navigateToLogin();
        }
    }

    // Navigate to home (news) view
    private void navigateToHome() {
        Bundle args = new Bundle();
        args.putString(Const.ARGS_TOKEN, userToken);
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_logoFragment_to_homeFragment, args);
    }

    // Navigate to login view
    private void navigateToLogin() {
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_logoFragment_to_loginFragment);
    }
}
