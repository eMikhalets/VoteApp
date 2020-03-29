package com.supercasual.fourtop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.supercasual.fourtop.network.ApiFactory;
import com.supercasual.fourtop.network.ApiService;
import com.supercasual.fourtop.network.CurrentUser;
import com.supercasual.fourtop.network.pojo.Login;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private String userLogin;
    private String userPass;

    private EditText etUserLogin;
    private EditText etUserPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserLogin = findViewById(R.id.et_login_userLogin);
        etUserPass = findViewById(R.id.et_login_userPass);
    }

    @Override
    protected void onPause() {
        super.onPause();
        userLogin = etUserLogin.getText().toString();
        userPass = etUserPass.getText().toString();
        callLogin();
    }

    public void onClickLoginBtn(View view) {
        switch (view.getId()) {
            case R.id.btn_login_login:
                finish();
                break;
            case R.id.btn_login_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    public void callLogin() {
        ApiService apiService = ApiFactory.getInstance().getApiService();
        RequestBody bodyLogin = RequestBody.create(MediaType.parse("text/plain"), userLogin);
        RequestBody bodyPass = RequestBody.create(MediaType.parse("text/plain"), userPass);

        apiService.login(bodyLogin, bodyPass).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.isSuccessful() && response.body() !=null) {
                    CurrentUser.setUserToken(response.body().getData().getUserToken());
                    CurrentUser.setLogin(userLogin);
                    Log.d("testLogs", "token: " + CurrentUser.getUserToken());
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.d("testLogs", t.toString());
                Log.d("testLogs", t.getMessage());
            }
        });
    }
}
