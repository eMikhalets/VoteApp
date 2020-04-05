package com.supercasual.fourtop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.supercasual.fourtop.adapter.UserAdapter;
import com.supercasual.fourtop.model.CurrentUser;
import com.supercasual.fourtop.model.User;

import java.util.ArrayList;
import java.util.List;

public class TopUsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_users);

        recyclerView = findViewById(R.id.recycler_top_users);

        List<User> users = new ArrayList<>();
        users.add(new User(CurrentUser.get().getLogin()));
        users.add(new User("Все остальные"));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(this, users);
        recyclerView.setAdapter(userAdapter);
    }
}
