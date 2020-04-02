package com.supercasual.fourtop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.supercasual.fourtop.model.CurrentUser;
import com.supercasual.fourtop.utils.Requests;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "debug_logs";

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    private String currentUserToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUserToken = CurrentUser.get().getToken();
        if (!checkUserToken(currentUserToken)) {
            startIntent(LoginActivity.class);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserToken(currentUserToken);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        logoutRequest();
    }

    public void onClickNavigation(View view) {
        switch (view.getId()) {
            case R.id.btn_main_home:
                startIntent(HomeActivity.class);
                break;
            case R.id.btn_main_profile:
                startIntent(ProfileActivity.class);
                break;
            case R.id.btn_main_user_images:
                startIntent(UserImagesActivity.class);
                break;
            case R.id.btn_main_voting:
                startIntent(VotingActivity.class);
                break;
            case R.id.btn_main_top_images:
                startIntent(TopImagesActivity.class);
                break;
            case R.id.btn_main_top_users:
                startIntent(TopUsersActivity.class);
                break;
            case R.id.btn_main_exit:
                logoutRequest();
                break;
        }
    }

    private void startIntent(Class cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
    }

    private boolean checkUserToken(String currentUserToken) {
        return !currentUserToken.equals("");
    }

    private void logoutRequest() {
        stringRequest = new StringRequest(Request.Method.POST, Requests.LOGOUT,
                response -> {
                    try {
                        int status = new JSONObject(response)
                                .getInt("status");
                        if (status == 200) {
                            MainActivity.this.finish();
                        } else if (status == 500) {
                            int errorCode = new JSONObject(response)
                                    .getInt("error_code");
                            String errorMsg = new JSONObject(response)
                                    .getString("error_msg");
                            String errorUCode = new JSONObject(response)
                                    .getString("error_ucode");
                            Toast.makeText(MainActivity.this,
                                    "status 500",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "status: " + status
                                    + "errorCode: " + errorCode + ", "
                                    + "errorMsg: " + errorMsg + ", "
                                    + "errorUCode: " + errorUCode);
                        } else {
                            Toast.makeText(MainActivity.this,
                                    "status: " + status,
                                    Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "status: " + status);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(LOG_TAG, "JSONException: " + e.toString());
                    }
                }, error -> Log.d(LOG_TAG, "onErrorResponse: " + error.toString()))
        {
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMapParams = new HashMap<>();
                hashMapParams.put("user_token", CurrentUser.get().getToken());
                return hashMapParams;
            }
        };
        requestQueue.add(stringRequest);
    }
}
