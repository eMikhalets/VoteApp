package com.supercasual.fourtop.uimain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentProfileBinding;
import com.supercasual.fourtop.model.CurrentUser;
import com.supercasual.fourtop.utils.Constants;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container,
                false);
        setUserInfo();
        return binding.getRoot();
    }

    private void setUserInfo() {
        binding.textProfileGreeting.setText(getString(R.string.profile_text_greeting,
                getArguments().getString(Constants.ARGS_LOGIN)));
        binding.textProfileUserToken.setText(getString(R.string.profile_text_user_token,
                getArguments().getString(Constants.ARGS_TOKEN)));
    }
}
