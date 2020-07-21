package com.ntech.fourtop.ui.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.ntech.fourtop.databinding.FragmentRegisterBinding;
import com.ntech.fourtop.utils.Const;

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

    private void errorObserver(String throwable) {
        binding.textErrorMessage.setText(throwable);
    }

    private void onRegisterClick() {
        Const.hideKeyboard(requireActivity());
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

    private void backToLogin() {
        Navigation.findNavController(binding.getRoot()).popBackStack();
    }
}