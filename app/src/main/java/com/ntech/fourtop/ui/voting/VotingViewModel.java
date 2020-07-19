package com.ntech.fourtop.ui.voting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ntech.fourtop.data.VotingRepository;
import com.ntech.fourtop.network.pojo.DataVoting;

public class VotingViewModel extends ViewModel {

    private VotingRepository votingRepository;

    private MutableLiveData<DataVoting> apiVoteCreate;
    private MutableLiveData<String> apiVote;

    private MutableLiveData<String> userToken;
    private MutableLiveData<String> voteToken;


    public VotingViewModel() {
        votingRepository = new VotingRepository();

        apiVoteCreate = new MutableLiveData<>();
        apiVote = new MutableLiveData<>();

        userToken = new MutableLiveData<>();
        voteToken = new MutableLiveData<>();
    }

    public LiveData<DataVoting> getApiVoteCreate() {
        return apiVoteCreate;
    }

    public LiveData<String> getApiVote() {
        return apiVote;
    }

    public MutableLiveData<String> getUserToken() {
        return userToken;
    }

    public MutableLiveData<String> getVoteToken() {
        return voteToken;
    }

    public void clearVoting() {
        apiVoteCreate.setValue(null);
    }

    public void voteCreateRequest() {
        String userToken = getUserToken().getValue();

        votingRepository.voteCreateRequest(userToken, new VotingRepository.VoteCreateCallback() {
            @Override
            public void success(DataVoting voting) {
                apiVoteCreate.setValue(voting);
                voteToken.setValue(voting.getVoteToken());
            }

            @Override
            public void failure(String result) {
            }
        });
    }

    public void voteRequest(int vote) {
        String userToken = getUserToken().getValue();
        String voteToken = getVoteToken().getValue();
        String voteStr = String.valueOf(vote);

        votingRepository.voteRequest(userToken, voteToken, voteStr,
                result -> apiVote.setValue(result));
    }
}
