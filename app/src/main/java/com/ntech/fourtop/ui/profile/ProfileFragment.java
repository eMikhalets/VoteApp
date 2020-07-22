package com.ntech.fourtop.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.ntech.fourtop.databinding.FragmentProfileBinding;
import com.ntech.fourtop.network.pojo.DataProfile;
import com.ntech.fourtop.utils.Const;

import org.jetbrains.annotations.NotNull;

public class ProfileFragment extends Fragment {

    private ProfileViewModel viewModel;
    private FragmentProfileBinding binding;

    private String token;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        viewModel.getLogout().observe(getViewLifecycleOwner(), this::logoutObserver);
        viewModel.getProfile().observe(getViewLifecycleOwner(), this::profileObserver);
        viewModel.getThrowable().observe(getViewLifecycleOwner(), this::errorObserver);
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), this::errorObserver);

        binding.btnLogout.setOnClickListener(v -> onLogoutClick());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void logoutObserver(int status) {
        deleteUserToken();
        navigateToLogin();
    }

    private void profileObserver(DataProfile profile) {
        binding.textTesterName.setText(profile.getTesterName());
        binding.textLogin.setText(profile.getLogin());
        binding.textEmail.setText(profile.getEmail());
    }

    private void errorObserver(String error) {
        binding.textErrorMessage.setText(error);
    }

    private void onLogoutClick() {
        viewModel.logoutRequest("");
    }

    private void deleteUserToken() {
        SharedPreferences sp = requireActivity().getSharedPreferences(
                Const.SHARED_FILE, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(Const.SHARED_TOKEN, "");
        editor.apply();
    }

    private void navigateToLogin() {
        Navigation.findNavController(binding.getRoot()).popBackStack();
    }
}
