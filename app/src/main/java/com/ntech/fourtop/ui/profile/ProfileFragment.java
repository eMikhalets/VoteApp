package com.ntech.fourtop.ui.profile;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ntech.fourtop.databinding.FragmentProfileBinding;
import com.ntech.fourtop.utils.Const;

import org.jetbrains.annotations.NotNull;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;

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

        viewModel.getApiProfile().observe(getViewLifecycleOwner(), s -> {
            if (!s.equals("OK")) {
                Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getApiLogout().observe(getViewLifecycleOwner(), s -> {
            if (s.equals("OK")) {
                deleteUserToken();
                requireActivity().finishAffinity();
            }
        });

        viewModel.getName().observe(getViewLifecycleOwner(),
                s -> binding.textTesterName.setText(s));

        viewModel.getLogin().observe(getViewLifecycleOwner(),
                s -> binding.textLogin.setText(s));

        viewModel.getEmail().observe(getViewLifecycleOwner(),
                s -> binding.textEmail.setText(s));

        binding.btnLogout.setOnClickListener(
                v -> viewModel.logoutRequest(viewModel.getToken().getValue()));

        setTokenFromArguments();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setTokenFromArguments() {
        Bundle args = getArguments();
        if (args != null) {
            String token = args.getString(Const.ARGS_TOKEN);
            viewModel.getToken().setValue(token);

            if (token != null && !token.isEmpty()) {
                viewModel.profileRequest(token);
            }
        }
    }

    private void deleteUserToken() {
        SharedPreferences sp = requireActivity().getSharedPreferences(
                Const.SHARED_FILE, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(Const.SHARED_TOKEN, "");
        editor.apply();
    }
}
