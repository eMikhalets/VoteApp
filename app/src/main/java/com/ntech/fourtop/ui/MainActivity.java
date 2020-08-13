package com.ntech.fourtop.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ntech.fourtop.R;
import com.ntech.fourtop.databinding.ActivityMainBinding;

import static androidx.navigation.Navigation.findNavController;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private AppBarConfiguration appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        navController = findNavController(this, R.id.main_nav_host);
        appBar = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBar);

        binding.navigationView.setNavigationItemSelectedListener(this::navigationListener);
        navController.addOnDestinationChangedListener(this::destinationChanged);

        switch (navController.getCurrentDestination().getId()) {
            case R.id.logoFragment:
            case R.id.loginFragment:
            case R.id.registerFragment:
                binding.toolbar.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBar) || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        int currentFragment = navController.getCurrentDestination().getId();

        switch (currentFragment) {
            case R.id.loginFragment:
            case R.id.homeFragment:
                finish();
                break;
            default:
                navController.popBackStack();
                break;
        }
    }

    private boolean navigationListener(MenuItem item) {
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
    }

    private void destinationChanged(@NonNull NavController controller,
                                    @NonNull NavDestination destination,
                                    @NonNull Bundle arguments) {
        if (destination.getId() == R.id.homeFragment) {
            binding.layoutDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            binding.layoutDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }
}
