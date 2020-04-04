package com.supercasual.fourtop;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.supercasual.fourtop.model.CurrentUser;
import com.supercasual.fourtop.utils.Network;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUserLogin;
    private EditText etUserPass;
    private EditText etConfirmPass;
    private EditText etName;

    private String userEmail;
    private String userLogin;
    private String userPass;
    private String userConfirmPass;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUserLogin = findViewById(R.id.et_register_user_login);
        etUserPass = findViewById(R.id.et_register_user_pass);
        etConfirmPass = findViewById(R.id.et_register_confirm_pass);
        etName = findViewById(R.id.et_register_user_name);
    }

    public void onClickRegisterBtn(View view) {
        switch (view.getId()) {
            case R.id.btn_register_request_register:
                userEmail = etUserLogin.getText().toString().trim();
                userLogin = userEmail.split("@")[0];
                userPass = etUserPass.getText().toString().trim();
                userConfirmPass = etConfirmPass.getText().toString().trim();
                userName = etName.getText().toString().trim();

                if (!userPass.equals(userConfirmPass)){
                    Toast.makeText(this, "Пароли не совпадают",
                            Toast.LENGTH_SHORT).show();
                } else if (userEmail.equals("") || userPass.equals("") || userName.equals("")) {
                    Toast.makeText(this, "Все поля должны быть заполнены",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Network.get(this).registerRequest(userEmail, userLogin, userPass, userName,
                            () -> {
                                CurrentUser.get().setLogin(userLogin);
                                CurrentUser.get().setPass(userPass);
                                RegisterActivity.this.finish();
                            });
                }
                break;
        }
    }
}
