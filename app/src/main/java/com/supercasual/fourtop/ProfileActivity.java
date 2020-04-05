package com.supercasual.fourtop;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.supercasual.fourtop.model.CurrentUser;

public class ProfileActivity extends AppCompatActivity {

    private TextView textGreeting;
    private TextView textUserToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textGreeting = findViewById(R.id.text_profile_greeting);
        textUserToken = findViewById(R.id.text_profile_user_token);
    }

    @Override
    protected void onResume() {
        super.onResume();
        textGreeting.setText(
                getString(R.string.profile_text_greeting, CurrentUser.get().getLogin()));
        textUserToken.setText(
                getString(R.string.profile_text_user_token, CurrentUser.get().getToken()));
    }
}
