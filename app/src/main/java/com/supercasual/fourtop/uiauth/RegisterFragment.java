package com.supercasual.fourtop.uiauth;

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
import com.supercasual.fourtop.databinding.FragmentRegisterBinding;
import com.supercasual.fourtop.network.ApiFactory;
import com.supercasual.fourtop.network.pojo.ApiResponse;
import com.supercasual.fourtop.utils.Constants;

import org.jetbrains.annotations.NotNull;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    private boolean isPassVisible = false;
    private boolean isConfPassVisible = false;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container,
                false);

        binding.btnRegisterRequestRegister.setOnClickListener(v -> registerRequest());

        binding.imageBtnRegisterShowPass.setOnClickListener(view -> setPassVisibility());

        binding.imageBtnRegisterShowConfPass.setOnClickListener(view -> setConfPassVisibility());

        return binding.getRoot();
    }

    private void registerRequest() {
        String userLogin = binding.editRegisterLogin.getText().toString().trim();
        String userEmail = binding.editRegisterEmail.getText().toString().trim();
        String userPass = binding.editRegisterPass.getText().toString().trim();
        String userConfirmPass = binding.editRegisterConfirmPass.getText().toString().trim();
        String testerNickname = binding.editRegisterNickname.getText().toString().trim();

        if (!userEmail.isEmpty()) {
            sendRegisterEmailRequest(userEmail);
        } else if (!userLogin.isEmpty()) {
            sendRegisterLoginRequest(userLogin);
        } else if (!userPass.equals(userConfirmPass)) {
            Toast.makeText(getContext(), R.string.register_toast_no_match_pass,
                    Toast.LENGTH_SHORT).show();
        } else if (userPass.isEmpty() || testerNickname.isEmpty()) {
            Toast.makeText(getContext(), R.string.register_toast_empty_editText,
                    Toast.LENGTH_SHORT).show();
        } else {
            sendRegisterRequest(userEmail, userLogin, userPass, testerNickname);
        }
    }

    private void sendRegisterRequest(String email, String login, String pass, String nickname) {
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody loginBody = RequestBody.create(MediaType.parse("text/plain"), login);
        RequestBody passBody = RequestBody.create(MediaType.parse("text/plain"), pass);
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), nickname);

        Call<ApiResponse> responseCall = ApiFactory.getApiFactory().getApiService()
                .register(emailBody, loginBody, passBody, nameBody);
        responseCall.enqueue(new Callback<ApiResponse>() {
                                 @Override
                                 public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                     // HTTP code is always == 200
                                     // check JSON "status"
                                     int code = response.body().getStatus();

                                     if (code == 200) {
                                         Bundle args = new Bundle();
                                         args.putString(Constants.ARGS_LOGIN, login);
                                         args.putString(Constants.ARGS_PASS, pass);
                                         Navigation.findNavController(binding.getRoot())
                                                 .navigate(R.id.action_registerFragment_to_loginFragment, args);
                                     } else {
                                         Toast.makeText(getContext(), R.string.register_toast_registerError,
                                                 Toast.LENGTH_SHORT).show();
                                     }
                                 }

                                 @Override
                                 public void onFailure(Call<ApiResponse> call, Throwable t) {
                                     t.printStackTrace();
                                 }
                             }
        );
    }

    private void sendRegisterEmailRequest(String email) {
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);

        Call<ApiResponse> responseCall = ApiFactory.getApiFactory().getApiService().registerLogin(emailBody);
        responseCall.enqueue(new Callback<ApiResponse>() {
                                 @Override
                                 public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                     // HTTP code is always == 200
                                     // check JSON "status"
                                     int code = response.body().getStatus();

                                     if (code != 200) {
                                         binding.textRegisterEmailBusy.setVisibility(View.VISIBLE);
                                     }
                                 }

                                 @Override
                                 public void onFailure(Call<ApiResponse> call, Throwable t) {
                                     t.printStackTrace();
                                 }
                             }
        );
    }

    private void sendRegisterLoginRequest(String login) {
        RequestBody loginBody = RequestBody.create(MediaType.parse("text/plain"), login);

        Call<ApiResponse> responseCall = ApiFactory.getApiFactory().getApiService().registerLogin(loginBody);
        responseCall.enqueue(new Callback<ApiResponse>() {
                                 @Override
                                 public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                     // HTTP code is always == 200
                                     // check JSON "status"
                                     int code = response.body().getStatus();

                                     if (code != 200) {
                                         binding.textRegisterLoginBusy.setVisibility(View.VISIBLE);
                                     }
                                 }

                                 @Override
                                 public void onFailure(Call<ApiResponse> call, Throwable t) {
                                     t.printStackTrace();
                                 }
                             }
        );
    }

    private void setPassVisibility() {
        if (isPassVisible) {
            binding.editRegisterPass
                    .setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.imageBtnRegisterShowPass
                    .setImageResource(R.drawable.ic_remove_eye_gray_24dp);
            isPassVisible = false;
        } else {
            binding.editRegisterPass
                    .setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            binding.imageBtnRegisterShowPass
                    .setImageResource(R.drawable.ic_remove_eye_black_24dp);
            isPassVisible = true;
        }
    }

    private void setConfPassVisibility() {
        if (isConfPassVisible) {
            binding.editRegisterConfirmPass
                    .setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.imageBtnRegisterShowConfPass
                    .setImageResource(R.drawable.ic_remove_eye_gray_24dp);
            isConfPassVisible = false;
        } else {
            binding.editRegisterConfirmPass
                    .setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            binding.imageBtnRegisterShowConfPass
                    .setImageResource(R.drawable.ic_remove_eye_black_24dp);
            isConfPassVisible = true;
        }
    }
}
