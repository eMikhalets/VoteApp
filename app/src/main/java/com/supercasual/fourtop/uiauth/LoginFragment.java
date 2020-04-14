package com.supercasual.fourtop.uiauth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentLoginBinding;
import com.supercasual.fourtop.network.ApiFactory;
import com.supercasual.fourtop.network.pojo.TokenResponse;
import com.supercasual.fourtop.uimain.MainActivity;
import com.supercasual.fourtop.utils.Constants;

import org.jetbrains.annotations.NotNull;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    //private LoginViewModel viewModel;

    private boolean isPassVisible;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container,
                false);
        //viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding.btnLoginRequestLogin.setOnClickListener(v -> sendLoginRequest());

        binding.btnLoginRegistration.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(R.id.action_loginFragment_to_registerFragment));

        binding.imageBtnLoginShowPass.setOnClickListener(view -> setPassVisibility());

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserInfo();
    }

    private void setUserInfo() {
        String login = "";
        String password = "";

        if (getArguments() != null) {
            login = getArguments().getString(Constants.ARGS_LOGIN);
            password = getArguments().getString(Constants.ARGS_PASS);
        }

        if (!login.isEmpty() && !password.isEmpty()) {
            binding.editLoginLogin.setText(login);
            binding.editLoginPass.setText(password);
        }
    }

    /**
     * Save token to SharedPreferences
     * Send Bundle (login, token) to mainActivity
     */
    private void sendLoginRequest() {
        String userLogin = binding.editLoginLogin.getText().toString().trim();
        String userPass = binding.editLoginPass.getText().toString().trim();

        if (userLogin.isEmpty() || userPass.isEmpty()) {
            Toast.makeText(getContext(), R.string.login_toast_empty_editText,
                    Toast.LENGTH_SHORT).show();
        } else {
            RequestBody loginBody = RequestBody.create(MediaType.parse("text/plain"), userLogin);
            RequestBody passBody = RequestBody.create(MediaType.parse("text/plain"), userPass);

            Call<TokenResponse> responseCall = ApiFactory.getApiFactory().getApiService().login(loginBody, passBody);
            responseCall.enqueue(new Callback<TokenResponse>() {
                                     @Override
                                     public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                                         // HTTP code is always == 200
                                         // check JSON "status"
                                         int code = response.body().getStatus();

                                         if (code == 200) {
                                             String userToken = response.body().getData().getUserToken();
                                             saveUserToken(userToken);

                                             Intent intent = new Intent(getContext(), MainActivity.class);
                                             intent.putExtra(Constants.ARGS_TOKEN, userToken);
                                             intent.putExtra(Constants.ARGS_LOGIN, userLogin);
                                             startActivity(intent);

//                                             Bundle args = new Bundle();
//                                             args.putString(Constants.ARGS_LOGIN, userLogin);
//                                             args.putString(Constants.ARGS_TOKEN, userToken);
//                                             Navigation.findNavController(binding.getRoot())
//                                                     .navigate(R.id.action_loginFragment_to_mainActivity, args);
                                         } else {
                                             // TODO: ПРОТЕСТИТЬ!!!!!!!!!!!! :))
                                             Toast.makeText(getContext(), response.body().getErrorMsg(),
                                                     Toast.LENGTH_SHORT).show();
                                         }
                                     }

                                     @Override
                                     public void onFailure(Call<TokenResponse> call, Throwable t) {
                                         t.printStackTrace();
                                     }
                                 }
            );
        }
    }

    private void setPassVisibility() {
        if (isPassVisible) {
            binding.editLoginPass
                    .setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.imageBtnLoginShowPass.setImageResource(R.drawable.ic_remove_eye_gray_24dp);
            isPassVisible = false;
        } else {
            binding.editLoginPass
                    .setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            binding.imageBtnLoginShowPass.setImageResource(R.drawable.ic_remove_eye_black_24dp);
            isPassVisible = true;
        }
    }

    private void saveUserToken(String userToken) {
        SharedPreferences sp = getActivity()
                .getSharedPreferences(Constants.SHARED_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.SHARED_TOKEN, userToken);
        editor.apply();
    }
}
