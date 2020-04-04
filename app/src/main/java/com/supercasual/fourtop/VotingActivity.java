package com.supercasual.fourtop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.squareup.picasso.Picasso;
import com.supercasual.fourtop.fragment.RetainVotingFragment;
import com.supercasual.fourtop.model.Image;
import com.supercasual.fourtop.utils.Network;

import java.util.ArrayList;
import java.util.List;

public class VotingActivity extends AppCompatActivity {

    private RetainVotingFragment retainedFragment;
    private FragmentManager fragmentManager;
    private List<Image> imageList;

    private ImageView image0;
    private ImageView image1;
    private Button btnVote;

    private int vote = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        imageList = new ArrayList<>();

        image0 = findViewById(R.id.image_voting_image_0);
        image1 = findViewById(R.id.image_voting_image_1);
        btnVote = findViewById(R.id.btn_voting_vote);

        fragmentManager = getSupportFragmentManager();
        retainedFragment = (RetainVotingFragment) fragmentManager.findFragmentByTag("data");

        if (retainedFragment == null) {
            retainedFragment = new RetainVotingFragment();
            fragmentManager.beginTransaction().add(retainedFragment, "data").commit();
            retainedFragment.setData(imageList);
        }
    }

    @Override
    protected void onResume() {
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
    protected void onDestroy() {
        super.onDestroy();
        retainedFragment.setData(imageList);
    }

    public void onClickVoting(View view) {
        if (!imageList.isEmpty()) {
            switch (view.getId()) {
                case R.id.image_voting_image_0:
                    handleImageClick(Network.VOTE_FIRST, "Выбрана первая фотография");
                    break;
                case R.id.image_voting_image_1:
                    handleImageClick(Network.VOTE_SECOND, "Выбрана вторая фотография");
                    break;
                case R.id.btn_voting_vote:
                    Network.get(this).voteRequest(vote,
                            () -> {
                                vote = 2;
                                imageList = getVotingImages();
                            });
                    break;
            }
        }
    }

    private List<Image> getVotingImages() {
        return Network.get(VotingActivity.this).voteCreateRequest(this::onResume);
    }

    private void handleImageClick(int imageVote, String message) {
        if (!imageList.isEmpty()) {
            vote = imageVote;
            btnVote.setEnabled(true);
            Toast.makeText(this, message,
                    Toast.LENGTH_SHORT).show();
        }
    }
}
