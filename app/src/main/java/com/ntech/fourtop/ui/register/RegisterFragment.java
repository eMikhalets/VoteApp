package com.ntech.fourtop.ui.register;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.ntech.fourtop.databinding.FragmentRegisterBinding;

import org.jetbrains.annotations.NotNull;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private RegisterViewModel viewModel;
    private boolean isPassConfirm;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        viewModel.getThrowable().observe(getViewLifecycleOwner(), this::errorObserver);
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), this::errorObserver);
        viewModel.getLiveDataEmail().observe(getViewLifecycleOwner(), this::checkEmailObserver);
        viewModel.getLiveDataLogin().observe(getViewLifecycleOwner(), this::checkLoginObserver);
        viewModel.getLiveDataRegister().observe(getViewLifecycleOwner(), this::registerObserver);

        binding.btnRegister.setOnClickListener(v -> onRegisterClick());

        binding.etLogin.addTextChangedListener(onLoginTextChanged());
        binding.etEmail.addTextChangedListener(onEmailTextChanged());
        binding.etPass.addTextChangedListener(onPassTextChanged());
        binding.etConfirmPass.addTextChangedListener(onConfPassTextChanged());
        binding.etNickname.addTextChangedListener(onNameTextChanged());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void registerObserver(int status) {
        backToLogin();
    }

    private void checkEmailObserver(int status) {
        if (status == 200) binding.tilEmail.setError("Почта занята");
    }

    private void checkLoginObserver(int status) {
        if (status == 200) binding.tilLogin.setError("Почта занята");
    }

    private void errorObserver(String message) {
        if (message.contains("Email")) {
            binding.tilEmail.setError(message);
        } else if (message.contains("Login")) {
            binding.tilLogin.setError(message);
        } else {
            binding.textErrorMessage.setText(message);
        }
    }

    private void onRegisterClick() {
        hideKeyboard();
        String email = binding.etEmail.getText().toString().trim();
        String login = binding.etLogin.getText().toString().trim();
        String pass = binding.etPass.getText().toString().trim();
        String passConf = binding.etConfirmPass.getText().toString().trim();
        String name = binding.etNickname.getText().toString().trim();

        if (email.isEmpty()) binding.tilLogin.setError("Введите почту");
        if (email.isEmpty()) binding.tilLogin.setError("Введите логин");
        if (email.isEmpty()) binding.tilPass.setError("Введите пароль");
        if (email.isEmpty()) binding.tilConfirmPass.setError("Введите пароль");
        if (email.isEmpty()) binding.tilNickname.setError("Введите никнейм");

        if (!pass.isEmpty() && !passConf.isEmpty()) {
            if (pass.equals(passConf)) {
                isPassConfirm = true;
            } else {
                isPassConfirm = false;
                binding.tilConfirmPass.setError("Пароли не совпадают");
            }
        }

        if (!email.isEmpty() && !login.isEmpty() && !pass.isEmpty() && !name.isEmpty() && isPassConfirm) {
            viewModel.checkEmail(email);
            viewModel.checkLogin(login);
            viewModel.register(email, login, pass, name);
        }
    }

    // Field's text change listeners
    private TextWatcher onLoginTextChanged() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.tilLogin.setError(null);
            }
        };
    }

    private TextWatcher onEmailTextChanged() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.tilEmail.setError(null);
            }
        };
    }

    private TextWatcher onPassTextChanged() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.tilPass.setError(null);
            }
        };
    }

    private TextWatcher onConfPassTextChanged() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.tilConfirmPass.setError(null);
            }
        };
    }

    private TextWatcher onNameTextChanged() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.tilNickname.setError(null);
            }
        };
    }

    private void backToLogin() {
        Navigation.findNavController(binding.getRoot()).popBackStack();
    }

    public void hideKeyboard() {
        if (requireActivity().getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity()
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(requireActivity().getCurrentFocus()
                    .getWindowToken(), 0);
        }
    }
}