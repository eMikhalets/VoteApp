package com.ntech.fourtop.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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

import com.ntech.fourtop.R;
import com.ntech.fourtop.databinding.FragmentLoginBinding;
import com.ntech.fourtop.utils.Const;

import org.jetbrains.annotations.NotNull;

public class LoginFragment extends Fragment {

    private LoginViewModel viewModel;
    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel.getThrowable().observe(getViewLifecycleOwner(), this::errorObserver);
        viewModel.getUserToken().observe(getViewLifecycleOwner(), this::userTokenObserver);
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), this::errorObserver);

        binding.btnLogin.setOnClickListener(v -> onLoginClick());
        binding.btnRegistration.setOnClickListener(v -> onRegisterClick());

        binding.etLogin.addTextChangedListener(onLoginTextChanged());
        binding.etPass.addTextChangedListener(onPassTextChanged());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void userTokenObserver(String userToken) {
        saveUserToken(userToken);
        navigateToHome(userToken);
    }

    private void errorObserver(String message) {
        binding.textErrorMessage.setText(message);
    }

    private void onLoginClick() {
        hideKeyboard();
        String login = binding.etLogin.getText().toString().trim();
        String pass = binding.etPass.getText().toString().trim();
        if (login.isEmpty()) binding.tilLogin.setError(getString(R.string.login_toast_empty_login));
        if (pass.isEmpty()) binding.tilPass.setError(getString(R.string.login_toast_empty_pass));
        if (!login.isEmpty() && !pass.isEmpty()) viewModel.loginRequest(login, pass);
    }

    private void onRegisterClick() {
        hideKeyboard();
        navigateToRegister();
    }

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

    // Navigate to home (news) view
    private void navigateToHome(String userToken) {
        Bundle args = new Bundle();
        args.putString(Const.ARGS_TOKEN, userToken);
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_loginFragment_to_homeFragment, args);
    }

    // Navigate to register view
    private void navigateToRegister() {
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_loginFragment_to_registerFragment);
    }

    // Save token to SharedPreferences
    public void saveUserToken(String token) {
        SharedPreferences sp = requireActivity().getSharedPreferences(
                Const.SHARED_FILE, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(Const.SHARED_TOKEN, token);
        editor.apply();
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
