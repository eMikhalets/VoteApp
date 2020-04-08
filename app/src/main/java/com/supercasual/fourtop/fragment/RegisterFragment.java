package com.supercasual.fourtop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.model.CurrentUser;
import com.supercasual.fourtop.utils.Network;

public class RegisterFragment extends Fragment {

    private Context context;
    private View view;

    private EditText editUserLogin;
    private EditText editUserEmail;
    private EditText editUserPass;
    private EditText editConfirmPass;
    private EditText editTesterNickname;
    private ImageButton imageBtnShowPass;
    private ImageButton imageBtnShowConfPass;
    private Button btnRegister;

    private String userLogin;
    private String userEmail;
    private String userPass;
    private String userConfirmPass;
    private String testerNickname;
    private boolean isPassVisible;
    private boolean isConfPassVisible;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
        context = view.getContext();

        editUserLogin = view.findViewById(R.id.edit_register_login);
        editUserEmail = view.findViewById(R.id.edit_register_email);
        editUserPass = view.findViewById(R.id.edit_register_pass);
        editConfirmPass = view.findViewById(R.id.edit_register_confirm_pass);
        editTesterNickname = view.findViewById(R.id.edit_register_nackname);
        imageBtnShowPass = view.findViewById(R.id.image_btn_register_show_pass);
        imageBtnShowConfPass = view.findViewById(R.id.image_btn_register_show_conf_pass);
        btnRegister = view.findViewById(R.id.btn_register_request_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserInfo();

                if (!userPass.equals(userConfirmPass)) {
                    Toast.makeText(context, "Пароли не совпадают",
                            Toast.LENGTH_SHORT).show();
                } else if (userEmail.equals("") || userPass.equals("") || testerNickname.equals("")) {
                    Toast.makeText(context, "Все поля должны быть заполнены",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Network.get(context).registerRequest(userEmail, userLogin, userPass, testerNickname,
                            () -> {
                                CurrentUser.get().setLogin(userLogin);
                                CurrentUser.get().setPass(userPass);
                                // TODO: go to login fragment
                            });
                }
            }
        });

        imageBtnShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditTextVisibility(editUserPass, imageBtnShowPass, isPassVisible);
            }
        });

        imageBtnShowConfPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditTextVisibility(editConfirmPass, imageBtnShowConfPass, isConfPassVisible);
            }
        });

        return view;
    }

    private void setUserInfo() {
        userLogin = editUserLogin.getText().toString().trim();
        userEmail = editUserEmail.getText().toString().trim();
        userPass = editUserPass.getText().toString().trim();
        userConfirmPass = editConfirmPass.getText().toString().trim();
        testerNickname = editTesterNickname.getText().toString().trim();
    }

    private void setEditTextVisibility(EditText editText, ImageButton imageButton,
                                       boolean isVisible) {
        if (isVisible) {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imageButton.setImageResource(R.drawable.ic_remove_eye_gray_24dp);
            isVisible = false;
        } else {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imageButton.setImageResource(R.drawable.ic_remove_eye_black_24dp);
            isVisible = true;
        }
    }
}
