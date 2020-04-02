package com.supercasual.fourtop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.supercasual.fourtop.model.CurrentUser;
import com.supercasual.fourtop.utils.Requests;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = "debug_logs";

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    private EditText etUserLogin;
    private EditText etUserPass;

    private String userLogin;
    private String userPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestQueue = Volley.newRequestQueue(this);

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
                    loginRequest();
                }
                break;
            case R.id.btn_login_register_activity:
                Intent intent = new Intent(
                        LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void loginRequest() {
        stringRequest = new StringRequest(Request.Method.POST, Requests.LOGIN,
                response -> {
                    try {
                        int status = new JSONObject(response)
                                .getInt("status");
                        int errorCode = new JSONObject(response)
                                .getInt("error_code");
                        String errorMsg = new JSONObject(response)
                                .getString("error_msg");
                        String errorUCode = new JSONObject(response)
                                .getString("error_ucode");
                        if (status == 500) {
                            Toast.makeText(LoginActivity.this,
                                    "status 500",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "status: " + status
                                    + "errorCode: " + errorCode + ", "
                                    + "errorMsg: " + errorMsg + ", "
                                    + "errorUCode: " + errorUCode);
                        } else {
                            String token = new JSONObject(response)
                                    .getJSONObject("data")
                                    .getString("user_token");
                            CurrentUser.get().setToken(token);
                            if (token.equals("")) {
                                Toast.makeText(LoginActivity.this,
                                        "Токен пользователя не установлен",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                LoginActivity.this.finish();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(LOG_TAG, "JSONException: " + e.toString());
                    }
                }, error -> Log.d(LOG_TAG, "onErrorResponse: " + error.toString()))
        {
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMapParams = new HashMap<>();
                hashMapParams.put("login", userLogin);
                hashMapParams.put("password", userPass);
                return hashMapParams;
            }
        };
        requestQueue.add(stringRequest);
    }
}
