package com.supercasual.fourtop.uiauth;

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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentLoginBinding;
import com.supercasual.fourtop.uimain.MainActivity;
import com.supercasual.fourtop.utils.Constants;

import org.jetbrains.annotations.NotNull;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;

    private String login = "";
    private String password = "";

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container,
                false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        setDataFromArguments();

        binding.btnLogin.setOnClickListener(v -> onBtnLoginClicked());
        binding.btnRegistration.setOnClickListener(v -> onBtnRegisterClicked());
        binding.etLogin.setOnFocusChangeListener((v, hasFocus) -> loginUnFocus(hasFocus));
        binding.etPass.setOnFocusChangeListener((v, hasFocus) -> passUnFocus(hasFocus));
    }

    private void loginUnFocus(boolean hasFocus) {
        if (!hasFocus) {
            login = binding.etLogin.getText().toString().trim();
            binding.tilLogin.setError(null);
        }
    }

    private void passUnFocus(boolean hasFocus) {
        if (!hasFocus) {
            password = binding.etPass.getText().toString().trim();
            binding.tilPass.setError(null);
        }
    }

    private void onBtnLoginClicked() {
        hideKeyboard();
        checkFieldsFilling();

        if (!login.isEmpty() && !password.isEmpty()) {
            clearFieldsFocus();
            viewModel.login(login, password);
            viewModel.getLiveData().observe(getViewLifecycleOwner(), appResponse -> {
                if (appResponse.getDataToken() != null) {
                    String token = appResponse.getDataToken().getUserToken();
                    saveUserToken(token);
                    openMainActivity(token);
                } else {
                    Toast.makeText(getContext(), getString(R.string.login_toast_login_failed),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void clearFieldsFocus() {
        if (binding.etLogin.hasFocus()) {
            binding.etLogin.clearFocus();
        }
        if (binding.etPass.hasFocus()) {
            binding.etPass.clearFocus();
        }
    }

    private void checkFieldsFilling() {
        if (login.isEmpty()) {
            binding.tilLogin.setError(getString(R.string.login_toast_empty_login));
        }
        if (password.isEmpty()) {
            binding.tilPass.setError(getString(R.string.login_toast_empty_pass));
        }
    }

    private void onBtnRegisterClicked() {
        hideKeyboard();
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_loginFragment_to_registerFragment);
    }

    private void openMainActivity(String userToken) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(Constants.ARGS_TOKEN, userToken);
        startActivity(intent);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        if (requireActivity().getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void setDataFromArguments() {
        Bundle args = getArguments();

        if (args != null) {
            login = args.getString(Constants.ARGS_LOGIN);
            password = args.getString(Constants.ARGS_PASS);
        }

        if (login != null && password != null) {
            binding.etLogin.setText(login);
            binding.etPass.setText(password);
        }
    }

    public void saveUserToken(String userToken) {
        SharedPreferences sp = requireActivity().getSharedPreferences(
                Constants.SHARED_FILE, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(Constants.SHARED_TOKEN, userToken);
        editor.apply();
    }
}
