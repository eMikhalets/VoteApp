package com.supercasual.fourtop.uimain;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentProfileBinding;
import com.supercasual.fourtop.network.pojo.AppResponse;
import com.supercasual.fourtop.utils.Constants;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private SharedPreferences sharedPreferences;

    private String login;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container,
                false);
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        setArguments();
        initSharedPreferences();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUserData();
        binding.btnProfileLogout.setOnClickListener(v -> {
            LiveData<AppResponse> liveData = viewModel.logout(token);

            liveData.observe(getViewLifecycleOwner(), appResponse -> {
                if (appResponse.getDataString().equals(token)) {
                    viewModel.deleteUserToken(sharedPreferences);
                    getActivity().finishAffinity();
                } else {
                    Toast.makeText(getContext(), "Some Error", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void setArguments() {
        Bundle args = this.getArguments();

        if (args != null) {
            login = getArguments().getString(Constants.ARGS_LOGIN);
            token = getArguments().getString(Constants.ARGS_TOKEN);
        }
    }

    private void initSharedPreferences() {
        SharedPreferences sp = getActivity()
                .getSharedPreferences(Constants.SHARED_FILE, Context.MODE_PRIVATE);
    }

    private void setUserData() {
        binding.textProfileGreeting.setText(getString(R.string.profile_text_greeting, login));
        binding.textProfileUserToken.setText(getString(R.string.profile_text_user_token, token));
    }
}
