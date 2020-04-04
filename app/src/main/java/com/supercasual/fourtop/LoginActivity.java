package com.supercasual.fourtop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.supercasual.fourtop.model.CurrentUser;
import com.supercasual.fourtop.utils.Network;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "debug_logs";

    private EditText etUserLogin;
    private EditText etUserPass;

    private String userLogin;
    private String userPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserLogin = findViewById(R.id.et_login_user_login);
        etUserPass = findViewById(R.id.et_login_user_pass);
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
                    Network.get(this).loginRequest(userLogin, userPass, new Network.VolleyCallBack() {
                        @Override
                        public void onSuccess() {
                            LoginActivity.this.finish();
                        }
                    });
                }
                break;
            case R.id.btn_login_register_activity:
                Intent intent = new Intent(
                        LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
