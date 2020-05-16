package com.supercasual.fourtop.uiauth.logo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentLogoBinding;
import com.supercasual.fourtop.uimain.MainActivity;
import com.supercasual.fourtop.utils.Constants;

import org.jetbrains.annotations.NotNull;

public class LogoFragment extends Fragment {

    private FragmentLogoBinding binding;
    private LogoViewModel viewModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(LogoViewModel.class);

        viewModel.getApiToken().observe(getViewLifecycleOwner(), s -> {
            if (s.equals("VALID")) {
                openMainScreen(viewModel.getUserToken().getValue());
            } else if (s.equals("INVALID")) {
                openLoginScreen();
            } else {
                binding.textError.setText(s);
            }
        });

        loadUserToken();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    /**
     * Get user token from SharedPreferences and set it in viewModel.
     */
    private void loadUserToken() {
        SharedPreferences sp = requireActivity().getSharedPreferences(
                Constants.SHARED_FILE, Context.MODE_PRIVATE);
        String token = sp.getString(Constants.SHARED_TOKEN, "");
        if (!token.isEmpty()) {
            viewModel.getUserToken().setValue(token);
            viewModel.tokenRequest(token);
        } else {
            openLoginScreen();
        }
    }

    /**
     * Go to MainActivity if token is exist
     * @param token user identifier string for interacting with the server
     */
    private void openMainScreen(String token) {
        if (token != null && !token.isEmpty()) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra(Constants.ARGS_TOKEN, token);
            startActivity(intent);
        }
    }

    /**
     * Navigate to LoginFragment
     */
    private void openLoginScreen() {
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_logoFragment_to_loginFragment);
    }
}
