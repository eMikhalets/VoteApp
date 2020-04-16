package com.supercasual.fourtop.uimain;

import android.graphics.Path;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.PathInterpolator;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;
import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentVotingBinding;
import com.supercasual.fourtop.network.pojo.ImagesData;
import com.supercasual.fourtop.utils.Constants;
import com.supercasual.fourtop.viewmodel.VotingViewModel;

import java.util.List;

public class VotingFragment extends Fragment {

    private FragmentVotingBinding binding;
    private VotingViewModel viewModel;
    private LiveData<List<ImagesData>> liveDataImages;
    private LiveData<String> liveDataVoteToken;

    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voting, container,
                false);
        viewModel = new ViewModelProvider(this).get(VotingViewModel.class);
        token = getArguments().getString(Constants.ARGS_TOKEN);

        startVoting();

        return binding.getRoot();
    }

    private void startVoting() {
        liveDataImages = viewModel.getLiveDataImages();
        liveDataVoteToken = viewModel.getLiveDataVoteToken();

        if (liveDataImages.getValue().isEmpty()) {
            viewModel.sendVoteCreateRequest(token);
        }

        liveDataImages.observe(getViewLifecycleOwner(), new Observer<List<ImagesData>>() {
            @Override
            public void onChanged(List<ImagesData> imagesData) {
                if (!imagesData.isEmpty()) {
                    Picasso.get().load(imagesData.get(0).getLink()).into(binding.imageVoting0);
                    Picasso.get().load(imagesData.get(1).getLink()).into(binding.imageVoting1);
                }
            }
        });

    }

    private void voteForImage(int vote) {
        String voteToken = liveDataVoteToken.getValue();
        viewModel.sendVoteRequest(token, voteToken, vote);
        viewModel.clearLiveData();
        startVoting();
    }
}
