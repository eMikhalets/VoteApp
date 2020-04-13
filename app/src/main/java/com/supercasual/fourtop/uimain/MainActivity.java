package com.supercasual.fourtop.uimain;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.ActivityMainBinding;
import com.supercasual.fourtop.model.CurrentUser;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private AppBarConfiguration appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        navController = Navigation.findNavController(this, R.id.main_nav_host);
        NavigationUI.setupActionBarWithNavController(this, navController, binding.layoutDrawer);
        appBar = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(binding.navigationView, navController);

        binding.navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_nav_profile:
                    navController.navigate(R.id.profileFragment);
                    break;
                case R.id.menu_nav_user_images:
                    navController.navigate(R.id.userImagesFragment);
                    break;
                case R.id.menu_nav_voting:
                    navController.navigate(R.id.votingFragment);
                    break;
                case R.id.menu_nav_top_images:
                    navController.navigate(R.id.topImagesFragment);
                    break;
                case R.id.menu_nav_top_users:
                    navController.navigate(R.id.topUsersFragment);
                    break;
            }
            NavigationUI.onNavDestinationSelected(item, navController);
            binding.layoutDrawer.closeDrawers();
            return true;
        });

        //checkUserToken();
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

    private void checkUserToken() {
        if (CurrentUser.get().getToken().equals("")) {
            navController.navigate(R.id.loginFragment);
        }
    }
}
