package com.ntech.fourtop.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.ntech.fourtop.R;
import com.ntech.fourtop.databinding.FragmentLoginBinding;
import com.ntech.fourtop.ui.MainActivity;
import com.ntech.fourtop.utils.Const;

import org.jetbrains.annotations.NotNull;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        setDataFromArguments();

        viewModel.getApiLogin().observe(getViewLifecycleOwner(), s -> {
            if (!s.matches("Code:") || !s.matches("Throwable:") || !s.equals("ERROR")) {
                saveUserToken(s);
                openMainActivity(s);
            } else {
                Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getLogin().observe(getViewLifecycleOwner(), s -> {
            if (s != null && !s.isEmpty()) {
                viewModel.setLoginIsValid(true);
            } else {
                viewModel.setLoginIsValid(false);
                binding.tilLogin.setError(getString(R.string.login_toast_empty_login));
            }
        });

        viewModel.getPassword().observe(getViewLifecycleOwner(), s -> {
            if (s != null && !s.isEmpty()) {
                viewModel.setPasswordIsValid(true);
            } else {
                viewModel.setPasswordIsValid(false);
                binding.tilPass.setError(getString(R.string.login_toast_empty_pass));
            }
        });

        binding.etLogin.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                viewModel.getLogin().setValue(binding.etLogin.getText().toString().trim());
                binding.tilLogin.setError(null);
            }
        });

        binding.etPass.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                viewModel.getPassword().setValue(binding.etPass.getText().toString().trim());
                binding.tilPass.setError(null);
            }
        });

        binding.btnLogin.setOnClickListener(v -> {
            //hideKeyboard();
            clearFieldsFocus();

            viewModel.loginRequest(
                    viewModel.getLogin().getValue(),
                    viewModel.getPassword().getValue());
        });

        binding.btnRegistration.setOnClickListener(v -> {
            hideKeyboard();
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_loginFragment_to_registerFragment);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void clearFieldsFocus() {
        if (binding.etLogin.hasFocus()) {
            binding.etLogin.clearFocus();
        }
        if (binding.etPass.hasFocus()) {
            binding.etPass.clearFocus();
        }
    }

    private void openMainActivity(String userToken) {
        hideKeyboard();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(Const.ARGS_TOKEN, userToken);
        startActivity(intent);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        if (requireActivity().getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Get arguments from bundle, set login and password in viewModel and editText fields
     */
    private void setDataFromArguments() {
        Bundle args = getArguments();
        String login = null;
        String password = null;

        if (args != null) {
            login = args.getString(Const.ARGS_LOGIN);
            password = args.getString(Const.ARGS_PASS);

            viewModel.getLogin().setValue(login);
            viewModel.getPassword().setValue(password);
        }

        if (login != null && password != null) {
            binding.etLogin.setText(login);
            binding.etPass.setText(password);
        }
    }

    /**
     * Save token in SharedPreferences
     *
     * @param token user identifier string for interacting with the server
     */
    public void saveUserToken(String token) {
        SharedPreferences sp = requireActivity().getSharedPreferences(
                Const.SHARED_FILE, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(Const.SHARED_TOKEN, token);
        editor.apply();
    }
}
