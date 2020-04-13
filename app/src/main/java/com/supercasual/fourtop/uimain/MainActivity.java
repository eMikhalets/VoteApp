package com.supercasual.fourtop;

import android.os.Bundle;
import android.view.Gravity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.supercasual.fourtop.model.CurrentUser;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private AppBarConfiguration appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.layout_drawer);
        navigationView = findViewById(R.id.navigation_view);

        navController = Navigation.findNavController(this, R.id.navigation_host);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        appBar = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(item -> {
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
            drawerLayout.closeDrawers();
            return true;
        });

        checkUserToken();
    }

    @Override
    public boolean onSupportNavigateUp() {
        int destinationId = navController.getCurrentDestination().getId();

        if (destinationId == R.id.homeFragment) {
            drawerLayout.openDrawer(GravityCompat.START);
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
