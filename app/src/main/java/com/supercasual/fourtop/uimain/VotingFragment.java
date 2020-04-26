package com.supercasual.fourtop.uimain;

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
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;
import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentVotingBinding;
import com.supercasual.fourtop.network.pojo.AppResponse;
import com.supercasual.fourtop.network.pojo.DataImages;
import com.supercasual.fourtop.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VotingFragment extends Fragment {

    private FragmentVotingBinding binding;
    private VotingViewModel viewModel;

    private String token;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voting, container,
                false);
        viewModel = new ViewModelProvider(this).get(VotingViewModel.class);
        setArguments();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (viewModel.getLiveData().getValue() != null) {
            startVoting();
        }

        binding.imageVoting0.setOnClickListener(v -> voteImage(0));
        binding.imageVoting1.setOnClickListener(v -> voteImage(1));
    }

    private void setArguments() {
        Bundle args = this.getArguments();

        if (args != null) {
            token = args.getString(Constants.ARGS_TOKEN);
        }
    }

    private void startVoting() {
        LiveData<AppResponse> liveData = viewModel.voteCreate(token);
        liveData.observe(getViewLifecycleOwner(), appResponse -> {
            setImages(appResponse.getDataVoting().getImages());
        });
    }

    private void setImages(List<DataImages> images) {
        Picasso.get().load(images.get(0).getLink()).into(binding.imageVoting0);
        Picasso.get().load(images.get(1).getLink()).into(binding.imageVoting1);
    }

    private void voteImage(int vote) {
        String voteToken = viewModel.getLiveData().getValue().getDataVoting().getVoteToken();
        String imageNumber = String.valueOf(vote);

        LiveData<AppResponse> liveData = viewModel.vote(token, voteToken, imageNumber);
        liveData.observe(getViewLifecycleOwner(), appResponse -> {
            Toast.makeText(getContext(), "Голосование за " + imageNumber,
                    Toast.LENGTH_SHORT).show();
            viewModel.clearLiveData();
            startVoting();
        });
    }
}
