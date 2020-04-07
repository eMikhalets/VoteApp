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

public class LoginFragment extends Fragment {

    private Context context;
    private View view;

    private EditText editUserLogin;
    private EditText editUserPass;
    private ImageButton imageBtnShowPass;
    private Button btnLogin;
    private Button btnRegister;

    private String userLogin;
    private String userPass;
    private boolean isPassVisible;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        context = view.getContext();

        editUserLogin = view.findViewById(R.id.edit_login_login);
        editUserPass = view.findViewById(R.id.edit_login_pass);
        btnLogin = view.findViewById(R.id.btn_login_request_login);
        btnRegister = view.findViewById(R.id.btn_login_registration);
        imageBtnShowPass = view.findViewById(R.id.image_btn_login_show_pass);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin = editUserLogin.getText().toString().trim();
                userPass = editUserPass.getText().toString().trim();

                if (userLogin.equals("") || userPass.equals("")) {
                    Toast.makeText(context, "Все поля должны быть заполнены",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Network.get(context).loginRequest(userLogin, userPass,
                            new Network.VolleyCallBack() {
                                @Override
                                public void onSuccess() {
                                    // TODO: go to home fragment
                                }
                            });
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(
//                        LoginActivity.this, RegisterActivity.class);
//                startActivity(intent);
                // TODO: go to register fragment
            }
        });

        imageBtnShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPassVisible) {
                    editUserPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageBtnShowPass.setImageResource(R.drawable.ic_remove_eye_gray_24dp);
                    isPassVisible = false;
                } else {
                    editUserPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageBtnShowPass.setImageResource(R.drawable.ic_remove_eye_black_24dp);
                    isPassVisible = true;
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        setUserInfo();
    }

    private void setUserInfo() {
        String tempLogin = CurrentUser.get().getLogin();
        String tempPass = CurrentUser.get().getPass();
        if (!tempLogin.equals("") && !tempPass.equals("")) {
            editUserLogin.setText(tempLogin);
            editUserPass.setText(tempPass);
        }
    }
}
