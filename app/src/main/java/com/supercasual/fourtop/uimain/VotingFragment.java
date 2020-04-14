package com.supercasual.fourtop.uimain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.FragmentVotingBinding;
import com.supercasual.fourtop.model.Image;
import com.supercasual.fourtop.network.Network;
import com.supercasual.fourtop.network.VolleyCallBack;

import java.util.ArrayList;
import java.util.List;

public class VotingFragment extends Fragment {

    private FragmentVotingBinding binding;
    private List<Image> imageList;

    private int vote = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container,
                false);
        imageList = new ArrayList<>();

        binding.imageVotingImage0.setOnClickListener(
                v -> handleImageClick(Network.VOTE_FIRST, "Выбрана первая фотография"));

        binding.imageVotingImage1.setOnClickListener(
                v -> handleImageClick(Network.VOTE_SECOND, "Выбрана вторая фотография"));

        binding.btnVotingVote.setOnClickListener(v -> Network.get(getContext()).voteRequest(vote,
                new VolleyCallBack() {
                    @Override
                    public void onSuccess() {
                        vote = 2;
                        imageList = getVotingImages();
                        // TODO: to onResume
                        //VotingActivity.this.onResume();
                    }
                }));

        return binding.getRoot();
    }

    private List<Image> getVotingImages() {
        return Network.get(getContext()).voteCreateRequest(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                // TODO: to onResume
                //VotingActivity.this.onResume();
            }
        });
    }

    private void handleImageClick(int imageVote, String message) {
        if (!imageList.isEmpty()) {
            vote = imageVote;
            binding.btnVotingVote.setEnabled(true);
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
