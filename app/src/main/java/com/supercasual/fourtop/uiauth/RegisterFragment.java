package com.supercasual.fourtop.uiauth;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentRegisterBinding;
import com.supercasual.fourtop.utils.Constants;

import org.jetbrains.annotations.NotNull;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private RegisterViewModel viewModel;

    private boolean isLoginBusy;
    private boolean isEmailBusy;
    private boolean isPassMatched;

    private String login = "";
    private String email = "";
    private String password = "";
    private String confPassword = "";
    private String nickname = "";

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container,
                false);
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnRegister.setOnClickListener(v -> onButtonRegisterClicked());
        binding.etLogin.setOnFocusChangeListener((v, hasFocus) -> loginUnFocus(hasFocus));
        binding.etEmail.setOnFocusChangeListener((v, hasFocus) -> emailUnFocus(hasFocus));
        binding.etPass.setOnFocusChangeListener((v, hasFocus) -> passUnFocus(hasFocus));
        binding.etConfirmPass.setOnFocusChangeListener((v, hasFocus) -> confPassUnFocus(hasFocus));
        binding.etNickname.setOnFocusChangeListener((v, hasFocus) -> nameUnFocus(hasFocus));
    }

    private void onButtonRegisterClicked() {
        hideKeyboard();
        checkFieldsFilling();

        if (!login.isEmpty() && !email.isEmpty() && !password.isEmpty() &&
                !confPassword.isEmpty() && !nickname.isEmpty() &&
                !isLoginBusy && !isEmailBusy && isPassMatched) {
            clearFieldsFocus();
            viewModel.register(email, login, password, nickname);
            viewModel.getLiveDataRegister().observe(getViewLifecycleOwner(), appResponse -> {
                String response = appResponse.getDataString();
                if (response.equals("200")) {
                    backToLoginFragment();
                } else {
                    Toast.makeText(getContext(), getString(R.string.register_toast_fail_registration),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void clearFieldsFocus() {
        if (binding.etLogin.hasFocus()) {
            binding.etLogin.clearFocus();
        }
        if (binding.etEmail.hasFocus()) {
            binding.etEmail.clearFocus();
        }
        if (binding.etPass.hasFocus()) {
            binding.etPass.clearFocus();
        }
        if (binding.etConfirmPass.hasFocus()) {
            binding.etConfirmPass.clearFocus();
        }
        if (binding.etNickname.hasFocus()) {
            binding.etNickname.clearFocus();
        }
    }

    private void checkFieldsFilling() {
        if (login.isEmpty()) {
            binding.tilLogin.setError(getString(R.string.register_toast_empty_login));
        }
        if (email.isEmpty()) {
            binding.tilEmail.setError(getString(R.string.register_toast_empty_email));
        }
        if (password.isEmpty()) {
            binding.tilPass.setError(getString(R.string.register_toast_empty_pass));
        }
        if (confPassword.isEmpty()) {
            binding.tilConfirmPass.setError(getString(R.string.register_toast_empty_conf_pass));
        }
        if (nickname.isEmpty()) {
            binding.tilNickname.setError(getString(R.string.register_toast_empty_name));
        }
    }

    private void loginUnFocus(boolean hasFocus) {
        if (!hasFocus) {
            login = binding.etLogin.getText().toString().trim();

            if (binding.tilLogin.getError() != null) {
                binding.tilLogin.setError(null);
            }

            viewModel.checkLogin(login);
            viewModel.getLiveDataLogin().observe(getViewLifecycleOwner(), appResponse -> {
                if (appResponse.getDataString().equals("200")) {
                    binding.tilLogin.setError("Логин занят");
                    isLoginBusy = true;
                } else {
                    isLoginBusy = false;
                }
            });
        }
    }

    private void emailUnFocus(boolean hasFocus) {
        if (!hasFocus) {
            email = binding.etEmail.getText().toString().trim();

            if (binding.tilEmail.getError() != null) {
                binding.tilEmail.setError(null);
            }

            viewModel.checkEmail(email);
            viewModel.getLiveDataEmail().observe(getViewLifecycleOwner(), appResponse -> {
                if (appResponse.getDataString().equals("200")) {
                    binding.tilLogin.setError("Почта занята");
                    isEmailBusy = true;
                } else {
                    isEmailBusy = false;
                }
            });
        }
    }

    private void passUnFocus(boolean hasFocus) {
        if (!hasFocus) {
            password = binding.etPass.getText().toString().trim();
            binding.tilPass.setError(null);
        }
    }

    private void confPassUnFocus(boolean hasFocus) {
        if (!hasFocus) {
            confPassword = binding.etConfirmPass.getText().toString().trim();

            if (!confPassword.equals(password)) {
                binding.tilConfirmPass.setError(getString(R.string.register_toast_no_match_pass));
                isPassMatched = false;
            } else {
                binding.tilConfirmPass.setError(null);
                isPassMatched = true;
            }
        }
    }

    private void nameUnFocus(boolean hasFocus) {
        if (!hasFocus) {
            nickname = binding.etNickname.getText().toString().trim();
            binding.tilNickname.setError(null);
        }
    }

    private void backToLoginFragment() {
        Bundle args = new Bundle();
        args.putString(Constants.ARGS_LOGIN, login);
        args.putString(Constants.ARGS_PASS, password);
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_registerFragment_to_loginFragment, args);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        if (requireActivity().getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }
}