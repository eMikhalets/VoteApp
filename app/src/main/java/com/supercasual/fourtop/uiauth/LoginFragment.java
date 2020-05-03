package com.supercasual.fourtop.uiauth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentLoginBinding;
import com.supercasual.fourtop.network.pojo.AppResponse;
import com.supercasual.fourtop.uimain.MainActivity;
import com.supercasual.fourtop.utils.Constants;

import org.jetbrains.annotations.NotNull;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private SharedPreferences sharedPreferences;
    private LoginViewModel viewModel;

    private boolean isPassVisible = false;
    private String login = "";
    private String password = "";

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container,
                false);
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setArguments();
        initSharedPreferences();

        binding.btnLogin.setOnClickListener(v -> {
            setUserDataFromFields();
            hideKeyboard();

            if (isFieldsFilled()) {
                LiveData<AppResponse> liveData = viewModel.login(login, password);
                liveData.observe(getViewLifecycleOwner(), appResponse -> {
                    if (appResponse.getDataString() != null) {
                        Toast.makeText(getContext(), getString(R.string.login_toast_login_failed),
                                Toast.LENGTH_SHORT).show();
                        viewModel.clearLiveDara();
                    } else {
                        viewModel.saveUserToken(appResponse.getDataToken().getUserToken(),
                                sharedPreferences);
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.putExtra(Constants.ARGS_TOKEN, login);
                        intent.putExtra(Constants.ARGS_LOGIN, password);
                        startActivity(intent);
                    }
                });
            } else {
                Toast.makeText(getContext(), getString(R.string.login_toast_empty_data),
                        Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnRegistration.setOnClickListener(v -> {
            hideKeyboard();
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_loginFragment_to_registerFragment);
        });

        binding.imageBtnShowPassword.setOnClickListener(v -> {
            if (isPassVisible) {
                hidePassword();
            } else {
                showPassword();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserInfo();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    private void setArguments() {
        Bundle args = this.getArguments();
        if (args != null) {
            login = args.getString(Constants.ARGS_LOGIN);
            password = args.getString(Constants.ARGS_PASS);
        }
    }

    private void setUserInfo() {
        if (isFieldsFilled()) {
            binding.etLogin.setText(login);
            binding.etPassword.setText(password);
        }
    }

    private void showPassword() {
        binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        binding.imageBtnShowPassword.setImageResource(R.drawable.ic_remove_eye_black_24dp);
        isPassVisible = true;
    }

    private void hidePassword() {
        binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        binding.imageBtnShowPassword.setImageResource(R.drawable.ic_remove_eye_gray_24dp);
        isPassVisible = false;
    }

    private void initSharedPreferences() {
        sharedPreferences = getContext().getSharedPreferences(Constants.SHARED_FILE, Context.MODE_PRIVATE);
    }

    private void setUserDataFromFields() {
        login = binding.etLogin.getText().toString().trim();
        password = binding.etPassword.getText().toString().trim();
    }

    private boolean isFieldsFilled() {
        return !login.isEmpty() && !password.isEmpty();
    }
}
