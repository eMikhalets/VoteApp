package com.supercasual.fourtop.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentProfileBinding;
import com.supercasual.fourtop.model.CurrentUser;

public class ProfileFragment extends Fragment {

    private static final int LAYOUT = R.layout.fragment_profile;

    FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.textProfileGreeting.setText(
                getString(R.string.profile_text_greeting, CurrentUser.get().getLogin()));
        binding.textProfileUserToken.setText(
                getString(R.string.profile_text_user_token, CurrentUser.get().getToken()));
    }
}
