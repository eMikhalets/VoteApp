package com.supercasual.fourtop.ui.register;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentRegisterBinding;
import com.supercasual.fourtop.model.CurrentUser;
import com.supercasual.fourtop.network.Network;

import org.jetbrains.annotations.NotNull;

public class RegisterFragment extends Fragment {

    private static final int LAYOUT = R.layout.fragment_register;

    private FragmentRegisterBinding binding;

    private String userLogin;
    private String userEmail;
    private String userPass;
    private String userConfirmPass;
    private String testerNickname;
    private boolean isPassVisible = false;
    private boolean isConfPassVisible = false;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);

        binding.btnRegisterRequestRegister.setOnClickListener(view -> {
            setUserInfo();

            if (!userPass.equals(userConfirmPass)) {
                Toast.makeText(getContext(), R.string.register_toast_no_match_pass,
                        Toast.LENGTH_SHORT).show();
            } else if (userEmail.equals("") || userPass.equals("") || testerNickname.equals("")) {
                Toast.makeText(getContext(), R.string.register_toast_empty_editText,
                        Toast.LENGTH_SHORT).show();
            } else {
                register(view);
            }
        });

        binding.imageBtnRegisterShowPass.setOnClickListener(view -> {
            if (isPassVisible) {
                binding.editRegisterPass
                        .setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.imageBtnRegisterShowPass
                        .setImageResource(R.drawable.ic_remove_eye_gray_24dp);
                isPassVisible = false;
            } else {
                binding.editRegisterPass
                        .setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.imageBtnRegisterShowPass
                        .setImageResource(R.drawable.ic_remove_eye_black_24dp);
                isPassVisible = true;
            }
        });

        binding.imageBtnRegisterShowConfPass.setOnClickListener(view -> {
            if (isConfPassVisible) {
                binding.editRegisterConfirmPass
                        .setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.imageBtnRegisterShowConfPass
                        .setImageResource(R.drawable.ic_remove_eye_gray_24dp);
                isConfPassVisible = false;
            } else {
                binding.editRegisterConfirmPass
                        .setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.imageBtnRegisterShowConfPass
                        .setImageResource(R.drawable.ic_remove_eye_black_24dp);
                isConfPassVisible = true;
            }
        });

        return binding.getRoot();
    }

    private void setUserInfo() {
        userLogin = binding.editRegisterLogin.getText().toString().trim();
        userEmail = binding.editRegisterEmail.getText().toString().trim();
        userPass = binding.editRegisterPass.getText().toString().trim();
        userConfirmPass = binding.editRegisterConfirmPass.getText().toString().trim();
        testerNickname = binding.editRegisterNickname.getText().toString().trim();
    }

    private void register(View view) {
        Network.get(getContext()).registerRequest(userEmail, userLogin, userPass, testerNickname,
                () -> {
                    CurrentUser.get().setLogin(userLogin);
                    CurrentUser.get().setPass(userPass);
                    Navigation.findNavController(view).popBackStack();
                });
    }
}
