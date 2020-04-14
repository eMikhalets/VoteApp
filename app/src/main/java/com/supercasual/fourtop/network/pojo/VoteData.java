package com.supercasual.fourtop.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VoteData {

    @SerializedName("photos")
    @Expose
    private List<ImagesData> images;
    @SerializedName("vote_token")
    @Expose
    private String voteToken;

    public List<ImagesData> getImages() {
        return images;
    }

    public void setImages(List<ImagesData> images) {
        this.images = images;
    }

    public String getVoteToken() {
        return voteToken;
    }

    public void setVoteToken(String voteToken) {
        this.voteToken = voteToken;
    }
}
