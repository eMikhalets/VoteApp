package com.supercasual.fourtop.uiauth;

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
import com.supercasual.fourtop.network.VolleyCallBack;

import org.jetbrains.annotations.NotNull;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    private boolean isPassVisible = false;
    private boolean isConfPassVisible = false;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);

        binding.btnRegisterRequestRegister.setOnClickListener(this::prepareRegisterRequest);

        binding.imageBtnRegisterShowPass.setOnClickListener(view -> setPassVisibility());

        binding.imageBtnRegisterShowConfPass.setOnClickListener(view -> setConfPassVisibility());

        return binding.getRoot();
    }

    private void prepareRegisterRequest(View view) {
        String userLogin = binding.editRegisterLogin.getText().toString().trim();
        String userEmail = binding.editRegisterEmail.getText().toString().trim();
        String userPass = binding.editRegisterPass.getText().toString().trim();
        String userConfirmPass = binding.editRegisterConfirmPass.getText().toString().trim();
        String testerNickname = binding.editRegisterNickname.getText().toString().trim();

        if (!userPass.equals(userConfirmPass)) {
            Toast.makeText(getContext(), R.string.register_toast_no_match_pass,
                    Toast.LENGTH_SHORT).show();
        } else if (userEmail.equals("") || userPass.equals("") || testerNickname.equals("")) {
            Toast.makeText(getContext(), R.string.register_toast_empty_editText,
                    Toast.LENGTH_SHORT).show();
        } else {
            sendRegisterRequest(userLogin, userEmail, userPass, testerNickname, view);
        }
    }

    private void sendRegisterRequest(String userLogin, String userEmail, String userPass,
                                     String testerNickname, View view) {
        Network.get(getContext()).registerRequest(userEmail, userLogin, userPass, testerNickname,
                new VolleyCallBack() {
                    @Override
                    public void onSuccess() {
                        CurrentUser.get().setLogin(userLogin);
                        CurrentUser.get().setPass(userPass);
                        Navigation.findNavController(view).popBackStack();
                    }
                });
    }

    private void setPassVisibility() {
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
    }

    private void setConfPassVisibility() {
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
    }
}
