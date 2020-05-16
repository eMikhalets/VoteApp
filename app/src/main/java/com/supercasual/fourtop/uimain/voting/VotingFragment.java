package com.supercasual.fourtop.uimain.voting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.supercasual.fourtop.databinding.FragmentVotingBinding;
import com.supercasual.fourtop.network.pojo.AppResponse;
import com.supercasual.fourtop.network.pojo.DataImage;
import com.supercasual.fourtop.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VotingFragment extends Fragment {

    private FragmentVotingBinding binding;
    private VotingViewModel viewModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVotingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(VotingViewModel.class);

        viewModel.getApiVoteCreate().observe(getViewLifecycleOwner(), voting -> {
            Glide.with(this)
                    .load(voting.getImages().get(0).getLink())
                    .into(binding.imageVoting0);
            Glide.with(this)
                    .load(voting.getImages().get(1).getLink())
                    .into(binding.imageVoting1);
        });

        viewModel.getApiVote().observe(getViewLifecycleOwner(), s -> {
            if (s.equals("OK")) {
                viewModel.voteCreateRequest();
            } else {
                Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageVoting0.setOnClickListener(v -> viewModel.voteRequest(0));

        binding.imageVoting1.setOnClickListener(v -> viewModel.voteRequest(1));

        fragmentIsLoaded();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void fragmentIsLoaded() {
        Bundle args = getArguments();
        if (args != null) {
            String token = args.getString(Constants.ARGS_TOKEN);

            if (token != null && !token.isEmpty()) {
                viewModel.getUserToken().setValue(token);

                if (viewModel.getApiVoteCreate().getValue() == null) {
                    viewModel.voteCreateRequest();
                }
            } else {
                Toast.makeText(requireContext(), "Нет токена", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
