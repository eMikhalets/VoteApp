package com.supercasual.fourtop.uiauth;

import android.content.Context;
import android.content.Intent;
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
import androidx.navigation.Navigation;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentLogoBinding;
import com.supercasual.fourtop.network.pojo.AppResponse;
import com.supercasual.fourtop.uimain.MainActivity;
import com.supercasual.fourtop.utils.Constants;

import org.jetbrains.annotations.NotNull;

public class LogoFragment extends Fragment {

    private FragmentLogoBinding binding;
    private LogoViewModel viewModel;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_logo, container,
                false);
        viewModel = ViewModelProviders.of(this).get(LogoViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewIsLoaded();
    }

    /**
     * Initialising ShapedPreferences, loading user token and send token request.
     * If user token is correct, return token and send it to MainActivity.
     * If user token isn't correct, start LoginFragment
     */
    public void viewIsLoaded() {
        initSharedPreferences();
        String userToken = viewModel.loadUserToken(sharedPreferences);
        LiveData<AppResponse> liveData = viewModel.checkUserToken(userToken);

        liveData.observe(getViewLifecycleOwner(), appResponse -> {
            if (appResponse.getDataString().equals("403")) {
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_logoFragment_to_loginFragment);
            } else if (appResponse.getDataString().matches("\\d{3}")) {
                Toast.makeText(getContext(), appResponse.getDataString(), Toast.LENGTH_SHORT).show();
            } else {
                // String contains user token
                startMainActivity(appResponse.getDataString());
            }
        });
    }

    private void initSharedPreferences() {
        sharedPreferences = getContext().getSharedPreferences(Constants.SHARED_FILE, Context.MODE_PRIVATE);
    }

    private void startMainActivity(String userToken) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(Constants.ARGS_TOKEN, userToken);
        startActivity(intent);
    }
}
