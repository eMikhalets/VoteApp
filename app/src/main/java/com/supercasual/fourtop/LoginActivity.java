package com.supercasual.fourtop;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.supercasual.fourtop.model.CurrentUser;
import com.supercasual.fourtop.utils.Network;

public class LoginActivity extends AppCompatActivity {

    private EditText etUserLogin;
    private EditText etUserPass;
    private ImageButton imageBtnShowPass;

    private String userLogin;
    private String userPass;
    private boolean isPassVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserLogin = findViewById(R.id.et_login_login);
        etUserPass = findViewById(R.id.et_login_pass);
        imageBtnShowPass = findViewById(R.id.image_btn_login_show_pass);

        isPassVisible = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String tempLogin = CurrentUser.get().getLogin();
        String tempPass = CurrentUser.get().getPass();
        if (!tempLogin.equals("") && !tempPass.equals("")) {
            etUserLogin.setText(tempLogin);
            etUserPass.setText(tempPass);
        }
    }

    public void onClickLoginBtn(View view) {
        switch (view.getId()) {
            case R.id.btn_login_request_login:
                userLogin = etUserLogin.getText().toString().trim();
                userPass = etUserPass.getText().toString().trim();

                if (userLogin.equals("") || userPass.equals("")) {
                    Toast.makeText(this, "Все поля должны быть заполнены",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Network.get(this).loginRequest(userLogin, userPass, LoginActivity.this::finish);
                }
                break;
            case R.id.btn_login_register_activity:
                Intent intent = new Intent(
                        LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.image_btn_login_show_pass:
                if (isPassVisible) {
                    etUserPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageBtnShowPass.setImageResource(R.drawable.ic_remove_eye_gray_24dp);
                    isPassVisible = false;
                } else {
                    etUserPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageBtnShowPass.setImageResource(R.drawable.ic_remove_eye_black_24dp);
                    isPassVisible = true;
                }
                break;
        }
    }
}
