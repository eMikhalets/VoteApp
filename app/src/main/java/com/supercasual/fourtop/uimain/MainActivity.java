package com.supercasual.fourtop.uimain;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.ActivityMainBinding;
import com.supercasual.fourtop.databinding.NavHeaderBinding;
import com.supercasual.fourtop.utils.Constants;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavHeaderBinding navHeaderBinding;
    private NavController navController;
    private AppBarConfiguration appBar;

    private String login;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        getIntentExtra();
        setNavHeaderInfo();

        navController = Navigation.findNavController(this, R.id.main_nav_host);
        NavigationUI.setupActionBarWithNavController(this, navController, binding.layoutDrawer);
        appBar = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(binding.navigationView, navController);

        binding.navigationView.setNavigationItemSelectedListener(item -> {
            Bundle args = new Bundle();
            args.putString(Constants.ARGS_TOKEN, token);

            switch (item.getItemId()) {
                case R.id.menu_nav_profile:
                    args.putString(Constants.ARGS_LOGIN, login);
                    navController.navigate(R.id.action_homeFragment_to_profileFragment, args);
                    break;
                case R.id.menu_nav_user_images:
                    navController.navigate(R.id.action_homeFragment_to_userImagesFragment, args);
                    break;
                case R.id.menu_nav_voting:
                    navController.navigate(R.id.action_homeFragment_to_votingFragment, args);
                    break;
                case R.id.menu_nav_top_images:
                    navController.navigate(R.id.action_homeFragment_to_topImagesFragment, args);
                    break;
                case R.id.menu_nav_top_users:
                    navController.navigate(R.id.action_homeFragment_to_topUsersFragment);
                    break;
            }

            NavigationUI.onNavDestinationSelected(item, navController);
            binding.layoutDrawer.closeDrawers();
            return true;
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        int destinationId = navController.getCurrentDestination().getId();

        if (destinationId == R.id.homeFragment) {
            binding.layoutDrawer.openDrawer(GravityCompat.START);
        } else {
            navController.popBackStack();
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getId() == R.id.homeFragment) {
            finishAffinity();
        } else {
            navController.popBackStack();
        }
    }

    private void getIntentExtra() {
        Intent intent = getIntent();
        login = intent.getStringExtra(Constants.ARGS_LOGIN);
        token = intent.getStringExtra(Constants.ARGS_TOKEN);
    }

    private void setNavHeaderInfo() {
        // TODO: don't work
        navHeaderBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.nav_header,
                binding.navigationView, false);
        navHeaderBinding.navHeaderTextToken.setText(token);
    }
}
