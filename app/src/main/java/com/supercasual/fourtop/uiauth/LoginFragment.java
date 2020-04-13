package com.supercasual.fourtop.uiauth;

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
import com.supercasual.fourtop.network.VolleyCallBack;

import org.jetbrains.annotations.NotNull;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    private boolean isPassVisible;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container,
                false);

        binding.btnLoginRequestLogin.setOnClickListener(this::getUserData);

        binding.btnLoginRegistration.setOnClickListener(
                view -> Navigation.findNavController(view)
                        .navigate(R.id.action_registerFragment_to_loginFragment));

        binding.imageBtnLoginShowPass.setOnClickListener(view -> setPassVisibility());

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

        if (!login.isEmpty() && !password.isEmpty()) {
            binding.editLoginLogin.setText(login);
            binding.editLoginPass.setText(password);
        }
    }

    private void getUserData(View view) {
        String userLogin = binding.editLoginLogin.getText().toString().trim();
        String userPass = binding.editLoginPass.getText().toString().trim();

        if (userLogin.isEmpty() || userPass.isEmpty()) {
            Toast.makeText(getContext(), R.string.login_toast_empty_editText,
                    Toast.LENGTH_SHORT).show();
        } else {
            sendLoginRequest(userLogin, userPass, view);
        }
    }

    private void sendLoginRequest(String userLogin, String userPass, View view) {
        Network.get(getContext()).loginRequest(userLogin, userPass, new VolleyCallBack() {
            @Override
            public void onSuccess() {
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                Navigation.findNavController(view)
                        .navigate(R.id.action_loginFragment_to_mainActivity);
            }
        });
    }

    private void setPassVisibility() {
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
    }
}
