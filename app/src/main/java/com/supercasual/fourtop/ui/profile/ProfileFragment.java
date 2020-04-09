package com.supercasual.fourtop.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.model.CurrentUser;

public class ProfileFragment extends Fragment {

    private Context context;

    private TextView textGreeting;
    private TextView textUserToken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = view.getContext();

        textGreeting = view.findViewById(R.id.text_profile_greeting);
        textUserToken = view.findViewById(R.id.text_profile_user_token);

        textGreeting.setText(
                getString(R.string.profile_text_greeting, CurrentUser.get().getLogin()));
        textUserToken.setText(
                getString(R.string.profile_text_user_token, CurrentUser.get().getToken()));

        return view;
    }
}
