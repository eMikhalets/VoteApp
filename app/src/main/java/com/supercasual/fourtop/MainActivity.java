package com.supercasual.fourtop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.supercasual.fourtop.model.CurrentUser;
import com.supercasual.fourtop.utils.Network;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!checkUserToken(CurrentUser.get().getToken())) {
            //startIntent(LoginActivity.class);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //checkUserToken(CurrentUser.get().getToken());
    }

//    public void onClickNavigation(View view) {
//        switch (view.getId()) {
//            case R.id.btn_main_home:
//                startIntent(HomeActivity.class);
//                break;
//            case R.id.btn_main_profile:
//                startIntent(ProfileActivity.class);
//                break;
//            case R.id.btn_main_user_images:
//                startIntent(UserImagesActivity.class);
//                break;
//            case R.id.btn_main_voting:
//                startIntent(VotingActivity.class);
//                break;
//            case R.id.btn_main_top_images:
//                startIntent(TopImagesActivity.class);
//                break;
//            case R.id.btn_main_top_users:
//                startIntent(TopUsersActivity.class);
//                break;
//            case R.id.btn_main_exit:
//                userLogout();
//                break;
//        }
//    }

    private void userLogout() {
        Network.get(this).logoutRequest(MainActivity.this::finish);
    }

    private void startIntent(Class cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
    }

    private boolean checkUserToken(String currentUserToken) {
        return !currentUserToken.equals("");
    }
}
