package com.supercasual.fourtop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.supercasual.fourtop.model.CurrentUser;

public class ProfileActivity extends AppCompatActivity {

    TextView textUserToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textUserToken = findViewById(R.id.text_profile_user_token);
    }

    @Override
    protected void onResume() {
        super.onResume();
        textUserToken.setText(CurrentUser.get().getToken());
    }
}
