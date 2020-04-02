package com.supercasual.fourtop;

import androidx.appcompat.app.AppCompatActivity;

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

public class RegisterActivity extends AppCompatActivity {

    private static final String LOG_TAG = "debug_logs";

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    private EditText etUserLogin;
    private EditText etUserPass;
    private EditText etConfirmPass;
    private EditText etName;

    private String userLogin;
    private String userPass;
    private String userConfirmPass;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        requestQueue = Volley.newRequestQueue(this);

        etUserLogin = findViewById(R.id.et_register_user_login);
        etUserPass = findViewById(R.id.et_register_user_pass);
        etConfirmPass = findViewById(R.id.et_register_confirm_pass);
        etName = findViewById(R.id.et_register_user_name);
    }

    public void onClickRegisterBtn(View view) {
        switch (view.getId()) {
            case R.id.btn_register_request_register:
                userLogin = etUserLogin.getText().toString().trim();
                userPass = etUserPass.getText().toString().trim();
                userConfirmPass = etConfirmPass.getText().toString().trim();
                userName = etName.getText().toString().trim();

                if (!userPass.equals(userConfirmPass)){
                    Toast.makeText(this, "Пароли не совпадают",
                            Toast.LENGTH_SHORT).show();
                } else if (userLogin.equals("") || userPass.equals("") || userName.equals("")) {
                    Toast.makeText(this, "Все поля должны быть заполнены",
                            Toast.LENGTH_SHORT).show();
                } else {
                    doRegisterRequest();
                }
                break;
        }
    }

    private void doRegisterRequest() {
        stringRequest = new StringRequest(Request.Method.POST, Requests.REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int status = new JSONObject(response)
                                    .getInt("status");
                            if (status == 200) {
                                CurrentUser.get().setLogin(userLogin.split("@")[0]);
                                CurrentUser.get().setPass(userPass);
                                RegisterActivity.this.finish();
                            } else if (status == 500) {
                                int errorCode = new JSONObject(response)
                                        .getInt("error_code");
                                String errorMsg = new JSONObject(response)
                                        .getString("error_msg");
                                String errorUCode = new JSONObject(response)
                                        .getString("error_ucode");
                                Toast.makeText(RegisterActivity.this,
                                        "status 500",
                                        Toast.LENGTH_SHORT).show();
                                Log.d(LOG_TAG, "status: " + status
                                        + "errorCode: " + errorCode + ", "
                                        + "errorMsg: " + errorMsg + ", "
                                        + "errorUCode: " + errorUCode);
                            } else {
                                Toast.makeText(RegisterActivity.this,
                                        "status: " + status,
                                        Toast.LENGTH_SHORT).show();
                                Log.d(LOG_TAG, "status: " + status);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(LOG_TAG, "JSONException: " + e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(LOG_TAG, "onErrorResponse: " + error.toString());
            }
        })
        {
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMapParams = new HashMap<>();
                hashMapParams.put("email", userLogin);
                hashMapParams.put("login", userLogin.split("@")[0]);
                hashMapParams.put("password", userPass);
                hashMapParams.put("tester_name", userName);
                return hashMapParams;
            }
        };
        requestQueue.add(stringRequest);
    }
}
