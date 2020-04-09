package com.supercasual.fourtop.ui.login;

import android.content.Context;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentLoginBinding;
import com.supercasual.fourtop.model.CurrentUser;
import com.supercasual.fourtop.network.Network;

import org.jetbrains.annotations.NotNull;

public class LoginFragment extends Fragment {

    private static final int LAYOUT = R.layout.fragment_login;

    private FragmentLoginBinding binding;

    private String userLogin;
    private String userPass;
    private boolean isPassVisible;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);

        binding.btnLoginRequestLogin.setOnClickListener(view -> {
                    userLogin = binding.editLoginLogin.getText().toString().trim();
                    userPass = binding.editLoginPass.getText().toString().trim();

                    if (userLogin.equals("") || userPass.equals("")) {
                        Toast.makeText(getContext(), R.string.login_toast_empty_editText,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        login(view);
                    }
                }
        );

        binding.btnLoginRegistration.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.registerFragment);
        });

        binding.imageBtnLoginShowPass.setOnClickListener(view -> {
            if (isPassVisible) {
                binding.editLoginPass
                        .setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.imageBtnLoginShowPass.setImageResource(R.drawable.ic_remove_eye_gray_24dp);
                isPassVisible = false;
            } else {
                binding.editLoginPass
                        .setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.imageBtnLoginShowPass.setImageResource(R.drawable.ic_remove_eye_black_24dp);
                isPassVisible = true;
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserInfo();
    }

    private void setUserInfo() {
        String login = CurrentUser.get().getLogin();
        String password = CurrentUser.get().getPass();
        if (!login.equals("") && !password.equals("")) {
            binding.editLoginLogin.setText(login);
            binding.editLoginPass.setText(password);
        }
    }

    private void login(View view) {
        Network.get(getContext()).loginRequest(userLogin, userPass,
                () -> {
                    InputMethodManager imm = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    Navigation.findNavController(view).popBackStack();
                });
    }
}
