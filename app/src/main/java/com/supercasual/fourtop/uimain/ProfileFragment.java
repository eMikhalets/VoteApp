package com.supercasual.fourtop.uimain;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentProfileBinding;
import com.supercasual.fourtop.utils.Constants;

import org.jetbrains.annotations.NotNull;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;

    private String token;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container,
                false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        setTokenFromArguments();
        binding.btnLogout.setOnClickListener(v -> onButtonLogoutClicked());
        viewIsLoaded();
    }

    private void viewIsLoaded() {
        viewModel.profile(token);
        viewModel.getLiveData().observe(getViewLifecycleOwner(), appResponse -> {
            if (appResponse.getDataProfile() != null) {
                binding.setViewModel(viewModel);
            } else {
                Toast.makeText(getContext(), getString(R.string.loading_error),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTokenFromArguments() {
        Bundle args = getArguments();
        if (args != null) {
            token = args.getString(Constants.ARGS_TOKEN);
        }
    }

    private void deleteUserToken() {
        SharedPreferences sp = requireActivity().getSharedPreferences(
                Constants.SHARED_FILE, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(Constants.SHARED_TOKEN, "");
        editor.apply();
    }

    private void onButtonLogoutClicked() {
        viewModel.logout(token);
        viewModel.getLiveData().observe(getViewLifecycleOwner(), appResponse -> {
            if (appResponse.getDataString().equals(token)) {
                deleteUserToken();
                requireActivity().finishAffinity();
            } else {
                // Temp
                Toast.makeText(getContext(), "Some Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
