package com.supercasual.fourtop.uimain;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentProfileBinding;
import com.supercasual.fourtop.network.ApiFactory;
import com.supercasual.fourtop.network.pojo.ApiResponse;
import com.supercasual.fourtop.utils.Constants;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private String login;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container,
                false);
        setUserData();
        binding.btnProfileLogout.setOnClickListener(v -> sendLogoutRequest());
        return binding.getRoot();
    }

    private void sendLogoutRequest() {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), token);

        Call<ApiResponse> responseCall = ApiFactory.getApiFactory().getApiService().logout(requestBody);
        responseCall.enqueue(new Callback<ApiResponse>() {
                                 @Override
                                 public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                     // HTTP code is always == 200
                                     // check JSON "status"
                                     int code = response.body().getStatus();

                                     if (code == 200) {
                                         deleteSharedUserToken();
                                     }
                                 }

                                 @Override
                                 public void onFailure(Call<ApiResponse> call, Throwable t) {
                                     t.printStackTrace();
                                 }
                             }
        );
    }

    private void deleteSharedUserToken() {
        SharedPreferences sp = getActivity().getSharedPreferences(Constants.SHARED_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.SHARED_TOKEN, "");
        editor.apply();
    }

    private void setUserData() {
        login = getArguments().getString(Constants.ARGS_LOGIN);
        token = getArguments().getString(Constants.ARGS_TOKEN);

        binding.textProfileGreeting.setText(getString(R.string.profile_text_greeting, login));
        binding.textProfileUserToken.setText(getString(R.string.profile_text_user_token, token));
    }
}
