package com.supercasual.fourtop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.adapter.UserAdapter;
import com.supercasual.fourtop.model.CurrentUser;
import com.supercasual.fourtop.model.User;

import java.util.ArrayList;
import java.util.List;

public class TopUsersFragment extends Fragment {

    private Context context;
    private View view;

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        context = view.getContext();

        recyclerView = view.findViewById(R.id.recycler_top_users);

        List<User> users = new ArrayList<>();
        users.add(new User(CurrentUser.get().getLogin()));
        users.add(new User("Все остальные"));

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        userAdapter = new UserAdapter(context, users);
        recyclerView.setAdapter(userAdapter);

        return view;
    }
}
