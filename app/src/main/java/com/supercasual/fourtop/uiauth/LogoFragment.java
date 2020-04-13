package com.supercasual.fourtop.uiauth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentLogoBinding;
import com.supercasual.fourtop.model.CurrentUser;
import com.supercasual.fourtop.network.Network;
import com.supercasual.fourtop.network.VolleyCallBack;

import org.jetbrains.annotations.NotNull;

public class LogoFragment extends Fragment {

    private FragmentLogoBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_logo, container,
                false);

        Network.get(getContext()).tokenRequest("", new VolleyCallBack() {
            @Override
            public void onSuccess(int status) {
                // просто имитация долгого запроса
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (status == 200) {
                    Navigation.findNavController(binding.getRoot())
                            .navigate(R.id.action_logoFragment_to_mainActivity);
                } else {
                    Navigation.findNavController(binding.getRoot())
                            .navigate(R.id.action_logoFragment_to_loginFragment);
                }
            }
        });
        return binding.getRoot();
    }
}
