package com.supercasual.fourtop.uiauth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentLogoBinding;
import com.supercasual.fourtop.network.ApiFactory;
import com.supercasual.fourtop.network.pojo.ApiResponse;
import com.supercasual.fourtop.uimain.MainActivity;
import com.supercasual.fourtop.utils.Constants;

import org.jetbrains.annotations.NotNull;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogoFragment extends Fragment {

    private FragmentLogoBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_logo, container,
                false);
        sendTokenRequest();
        return binding.getRoot();
    }

    private String loadUserToken() {
        SharedPreferences sp = getActivity()
                .getSharedPreferences(Constants.SHARED_FILE, Context.MODE_PRIVATE);
        return sp.getString(Constants.SHARED_TOKEN, "");
    }

    private void sendTokenRequest() {
        String userToken = loadUserToken();
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), userToken);

        Call<ApiResponse> responseCall = ApiFactory.getApiFactory().getApiService().token(requestBody);
        responseCall.enqueue(new Callback<ApiResponse>() {
                                 @Override
                                 public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                     // HTTP code is always == 200
                                     // check JSON "status"
                                     int code = response.body().getStatus();

                                     if (code == 200) {
                                         Intent intent = new Intent(getContext(), MainActivity.class);
                                         intent.putExtra(Constants.ARGS_TOKEN, userToken);
                                         startActivity(intent);
//                                         Bundle args = new Bundle();
//                                         args.putString(Constants.ARGS_TOKEN, userToken);
//                                         Navigation.findNavController(binding.getRoot())
//                                                 .navigate(R.id.action_logoFragment_to_mainActivity, args);
                                     } else {
                                         Navigation.findNavController(binding.getRoot())
                                                 .navigate(R.id.action_logoFragment_to_loginFragment);
                                     }
                                 }

                                 @Override
                                 public void onFailure(Call<ApiResponse> call, Throwable t) {
                                     t.printStackTrace();
                                 }
                             }
        );
    }
}
