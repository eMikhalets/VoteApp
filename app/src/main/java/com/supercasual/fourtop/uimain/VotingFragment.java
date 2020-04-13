package com.supercasual.fourtop.uimain;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.squareup.picasso.Picasso;
import com.supercasual.fourtop.R;
import com.supercasual.fourtop.fragment.RetainVotingFragment;
import com.supercasual.fourtop.model.Image;
import com.supercasual.fourtop.network.Network;
import com.supercasual.fourtop.network.VolleyCallBack;

import java.util.ArrayList;
import java.util.List;

public class VotingFragment extends Fragment {

    private Context context;
    private View view;

    private RetainVotingFragment retainedFragment;
    private FragmentManager fragmentManager;
    private List<Image> imageList;

    private ImageView image0;
    private ImageView image1;
    private Button btnVote;

    private int vote = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        context = view.getContext();

        imageList = new ArrayList<>();

        image0 = view.findViewById(R.id.image_voting_image_0);
        image1 = view.findViewById(R.id.image_voting_image_1);
        btnVote = view.findViewById(R.id.btn_voting_vote);

        image0.setOnClickListener(
                v -> handleImageClick(Network.VOTE_FIRST, "Выбрана первая фотография"));

        image1.setOnClickListener(
                v -> handleImageClick(Network.VOTE_SECOND, "Выбрана вторая фотография"));

        btnVote.setOnClickListener(v -> Network.get(context).voteRequest(vote,
                new VolleyCallBack() {
                    @Override
                    public void onSuccess() {
                        vote = 2;
                        imageList = getVotingImages();
                        retainedFragment.setData(imageList);
                        // TODO: to onResume
                        //VotingActivity.this.onResume();
                    }
                }));

        // TODO: handle rotation
        //fragmentManager = view.getSupportFragmentManager();
        retainedFragment = (RetainVotingFragment) fragmentManager.findFragmentByTag("data");

        if (retainedFragment == null) {
            retainedFragment = new RetainVotingFragment();
            fragmentManager.beginTransaction().add(retainedFragment, "data").commit();
            retainedFragment.setData(imageList);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!retainedFragment.getData().isEmpty()) {
            imageList = retainedFragment.getData();
            Picasso.get().load(imageList.get(0).getImageURL()).into(image0);
            Picasso.get().load(imageList.get(1).getImageURL()).into(image1);
        } else if (!imageList.isEmpty()) {
            retainedFragment.setData(imageList);
            Picasso.get().load(imageList.get(0).getImageURL()).into(image0);
            Picasso.get().load(imageList.get(1).getImageURL()).into(image1);
        } else {
            imageList = getVotingImages();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        retainedFragment.setData(imageList);
    }

    private List<Image> getVotingImages() {
        return Network.get(context).voteCreateRequest(new VolleyCallBack() {
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
            btnVote.setEnabled(true);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
