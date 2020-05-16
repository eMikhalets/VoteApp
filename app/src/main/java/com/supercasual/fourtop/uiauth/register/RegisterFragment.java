package com.supercasual.fourtop.uiauth.register;

import android.app.Activity;
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

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentRegisterBinding;
import com.supercasual.fourtop.utils.Constants;

import org.jetbrains.annotations.NotNull;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private RegisterViewModel viewModel;

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

        viewModel.getApiRegister().observe(getViewLifecycleOwner(), s -> {
            if (s.equals("OK")) {
                backToLoginFragment(
                        viewModel.getLogin().getValue(),
                        viewModel.getPassword().getValue());
            } else {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getApiEmailCheck().observe(getViewLifecycleOwner(), s -> {
            if (s.equals("OK")) {
                viewModel.setEmailIsFree(true);
                binding.tilEmail.setError(null);
            } else {
                viewModel.setEmailIsFree(false);
                binding.tilEmail.setError("Почта занята");
            }
        });

        viewModel.getApiLoginCheck().observe(getViewLifecycleOwner(), s -> {
            if (s.equals("OK")) {
                viewModel.setLoginIsFree(true);
                binding.tilLogin.setError(null);
            } else {
                viewModel.setLoginIsFree(false);
                binding.tilLogin.setError("Логин занят");
            }
        });

        viewModel.getEmail().observe(getViewLifecycleOwner(), s -> {
            if (s != null && !s.isEmpty()) {
                viewModel.setEmailIsValid(true);
                binding.tilEmail.setError(null);
                viewModel.checkEmail(s);
            } else {
                viewModel.setEmailIsValid(false);
                binding.tilEmail.setError("Введите почту");
            }
        });

        viewModel.getLogin().observe(getViewLifecycleOwner(), s -> {
            if (s != null && !s.isEmpty()) {
                viewModel.setLoginIsValid(true);
                binding.tilLogin.setError(null);
                viewModel.checkLogin(s);
            } else {
                viewModel.setLoginIsValid(false);
                binding.tilLogin.setError("Введите логин");
            }
        });

        viewModel.getPassword().observe(getViewLifecycleOwner(), s -> {
            if (s != null && !s.isEmpty()) {
                viewModel.setPassIsValid(true);
                binding.tilPass.setError(null);
                viewModel.checkPassMatch();
            } else {
                viewModel.setPassIsValid(false);
                binding.tilPass.setError("Введите пароль");
            }
        });

        viewModel.getConfPass().observe(getViewLifecycleOwner(), s -> {
            if (s != null && !s.isEmpty()) {
                viewModel.setConfPassIsValid(true);
                binding.tilConfirmPass.setError(null);
                viewModel.checkPassMatch();
            } else {
                viewModel.setConfPassIsValid(false);
                binding.tilConfirmPass.setError("Подтвердите пароль");
            }
        });

        viewModel.getName().observe(getViewLifecycleOwner(), s -> {
            if (s != null && !s.isEmpty()) {
                viewModel.setNameIsValid(true);
                binding.tilNickname.setError(null);
            } else {
                viewModel.setNameIsValid(false);
                binding.tilLogin.setError("Введите никнейм");
            }
        });

        binding.btnRegister.setOnClickListener(v -> {
            //hideKeyboard();
            clearFieldsFocus();

            viewModel.checkEmail(viewModel.getEmail().getValue());
            viewModel.checkLogin(viewModel.getLogin().getValue());
            viewModel.register(
                    viewModel.getEmail().getValue(),
                    viewModel.getLogin().getValue(),
                    viewModel.getPassword().getValue(),
                    viewModel.getName().getValue());
        });

        binding.etLogin.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                viewModel.getLogin().setValue(binding.etLogin.getText().toString().trim());
            }
        });

        binding.etEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                viewModel.getEmail().setValue(binding.etEmail.getText().toString().trim());
            }
        });

        binding.etPass.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                viewModel.getPassword().setValue(binding.etPass.getText().toString().trim());
            }
        });

        binding.etConfirmPass.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                viewModel.getConfPass().setValue(binding.etConfirmPass.getText().toString().trim());
            }
        });

        binding.etNickname.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                viewModel.getName().setValue(binding.etNickname.getText().toString().trim());
            }
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

    private void backToLoginFragment(String login, String password) {
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