package com.supercasual.fourtop.uiauth;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentRegisterBinding;
import com.supercasual.fourtop.network.pojo.AppResponse;
import com.supercasual.fourtop.utils.Constants;

import org.jetbrains.annotations.NotNull;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private RegisterViewModel viewModel;

    private boolean isPassVisible = false;
    private boolean isConfPassVisible = false;
    private String login;
    private String email;
    private String password;
    private String confPassword;
    private String nickname;

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

        // TODO: email and login responses
        binding.btnRegister.setOnClickListener(v -> {
            setUserDataFromFields();

            if (isFieldsFilled()) {
                if (!isPasswordsMatched()) {
                    Toast.makeText(getContext(), getString(R.string.register_toast_no_match_pass),
                            Toast.LENGTH_SHORT).show();
                } else {
                    LiveData<AppResponse> liveData = viewModel.register(email, login, password, nickname);

                    liveData.observe(getViewLifecycleOwner(), appResponse -> {
                        if (appResponse.getDataString().equals("500")) {
                            Toast.makeText(getContext(), getString(R.string.register_toast_register_failed),
                                    Toast.LENGTH_SHORT).show();
                        } else if (appResponse.getDataString().matches("\\d{3}")) {
                            Toast.makeText(getContext(), appResponse.getDataString(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            backToLoginFragment();
                        }
                    });
                }
            } else {
                Toast.makeText(getContext(), getString(R.string.register_toast_empty_fields),
                        Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageBtnShowPass.setOnClickListener(v -> {
            if (isPassVisible) {
                hidePassword();
            } else {
                showPassword();
            }
        });

        binding.imageBtnShowConfPass.setOnClickListener(v -> {
            if (isConfPassVisible) {
                hideConfirmPassword();
            } else {
                showConfirmPassword();
            }
        });
    }

    private void backToLoginFragment() {
        Bundle args = new Bundle();
        args.putString(Constants.ARGS_LOGIN, login);
        args.putString(Constants.ARGS_PASS, password);
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_registerFragment_to_loginFragment, args);
    }

    private void setUserDataFromFields() {
        login = binding.etLogin.getText().toString().trim();
        email = binding.etEmail.getText().toString().trim();
        password = binding.etPassword.getText().toString().trim();
        confPassword = binding.etConfirmPass.getText().toString().trim();
        nickname = binding.etNickname.getText().toString().trim();
    }

    private boolean isPasswordsMatched() {
        return password.equals(confPassword);
    }

    private boolean isFieldsFilled() {
        return !login.isEmpty() && !email.isEmpty() && !password.isEmpty()
                && !confPassword.isEmpty() && nickname.isEmpty();
    }

    private void showPassword() {
        binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        binding.imageBtnShowPass.setImageResource(R.drawable.ic_remove_eye_black_24dp);
        isPassVisible = true;
    }

    private void hidePassword() {
        binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        binding.imageBtnShowPass.setImageResource(R.drawable.ic_remove_eye_gray_24dp);
        isPassVisible = false;
    }

    private void showConfirmPassword() {
        binding.etConfirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        binding.imageBtnShowConfPass.setImageResource(R.drawable.ic_remove_eye_black_24dp);
        isConfPassVisible = true;
    }

    private void hideConfirmPassword() {
        binding.etConfirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        binding.imageBtnShowConfPass.setImageResource(R.drawable.ic_remove_eye_gray_24dp);
        isConfPassVisible = false;
    }
}
