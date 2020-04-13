package com.supercasual.fourtop.uimain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.adapter.UserAdapter;
import com.supercasual.fourtop.databinding.FragmentTopUsersBinding;
import com.supercasual.fourtop.model.CurrentUser;
import com.supercasual.fourtop.model.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TopUsersFragment extends Fragment {

    private static final int LAYOUT = R.layout.fragment_top_users;

    private FragmentTopUsersBinding binding;

    private UserAdapter userAdapter;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);
        binding.recyclerTopUsers.setHasFixedSize(true);

        List<User> users = new ArrayList<>();
        users.add(new User(CurrentUser.get().getLogin()));
        users.add(new User("Все остальные"));

        binding.recyclerTopUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        userAdapter = new UserAdapter(getContext(), users);
        binding.recyclerTopUsers.setAdapter(userAdapter);

        return binding.getRoot();
    }
}
