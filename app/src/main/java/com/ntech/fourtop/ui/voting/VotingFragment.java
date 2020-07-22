package com.ntech.fourtop.ui.voting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ntech.fourtop.databinding.FragmentVotingBinding;

import org.jetbrains.annotations.NotNull;

public class VotingFragment extends Fragment {

    private VotingViewModel viewModel;
    private FragmentVotingBinding binding;

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
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), this::errorObserver);
        viewModel.getThrowable().observe(getViewLifecycleOwner(), this::errorObserver);
        viewModel.getVoting().observe(getViewLifecycleOwner(), this::votingObserver);

        binding.imageVoting0.setOnClickListener(v -> onClickFirstImage());
        binding.imageVoting1.setOnClickListener(v -> onClickSecondImage());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void errorObserver(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }

    private void votingObserver(int status) {
        viewModel.voteCreateRequest();
    }

    private void onClickFirstImage() {
        viewModel.voteRequest(0);
    }

    private void onClickSecondImage() {
        viewModel.voteRequest(1);
    }
}
