package com.supercasual.fourtop.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataVoting {

    @SerializedName("photos")
    @Expose
    private List<DataImages> images;
    @SerializedName("vote_token")
    @Expose
    private String voteToken;

    public List<DataImages> getImages() {
        return images;
    }

    public void setImages(List<DataImages> images) {
        this.images = images;
    }

    public String getVoteToken() {
        return voteToken;
    }

    public void setVoteToken(String voteToken) {
        this.voteToken = voteToken;
    }
}
