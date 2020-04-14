package com.supercasual.fourtop.uiauth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.ActivityAuthBinding;

public class AuthActivity extends AppCompatActivity {

    private ActivityAuthBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth);

        navController = Navigation.findNavController(this, R.id.auth_nav_host);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (navController.getCurrentDestination().getId() == R.id.registerFragment) {
            // TODO: set slide_right_out anim
            navController.navigate(R.id.action_registerFragment_to_loginFragment);
        }
    }
}
