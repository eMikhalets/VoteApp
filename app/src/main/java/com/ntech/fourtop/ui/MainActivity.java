package com.ntech.fourtop.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ntech.fourtop.R;
import com.ntech.fourtop.databinding.ActivityMainBinding;
import com.ntech.fourtop.databinding.NavHeaderBinding;
import com.ntech.fourtop.ui.home.HomeFragment;
import com.ntech.fourtop.ui.home.HomeFragmentDirections;
import com.ntech.fourtop.utils.Const;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private AppBarConfiguration appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navController = Navigation.findNavController(this, R.id.main_nav_host);
        NavigationUI.setupActionBarWithNavController(this, navController, binding.layoutDrawer);
        appBar = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(binding.navigationView, navController);

        binding.navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_nav_profile:
                    navController.navigate(R.id.action_homeFragment_to_profileFragment);
                    break;
                case R.id.menu_nav_user_images:
                    navController.navigate(R.id.action_homeFragment_to_userImagesFragment);
                    break;
                case R.id.menu_nav_voting:
                    navController.navigate(R.id.action_homeFragment_to_votingFragment);
                    break;
                case R.id.menu_nav_top_images:
                    navController.navigate(R.id.action_homeFragment_to_topImagesFragment);
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
        int currentFragment = navController.getCurrentDestination().getId();

        switch (currentFragment) {
            case R.id.loginFragment:
            case R.id.homeFragment:
                finish();
        }
    }
}
