package com.supercasual.fourtop.network.pojo;

import java.util.List;

public class AppResponse {

    private String dataString;
    private List<DataImages> dataImages;
    private DataToken dataToken;
    private DataVoting dataVoting;
    private DataProfile dataProfile;

    /**
     * Constructors
     */
    public AppResponse() {
    }

    public AppResponse(String dataString) {
        this.dataString = dataString;
    }

    public AppResponse(List<DataImages> dataImages) {
        this.dataImages = dataImages;
    }

    public AppResponse(DataToken dataToken) {
        this.dataToken = dataToken;
    }

    public AppResponse(DataVoting dataVoting) {
        this.dataVoting = dataVoting;
    }

    public AppResponse(DataProfile dataProfile) {
        this.dataProfile = dataProfile;
    }

    /**
     * Getters and setters
     */
    public String getDataString() {
        return dataString;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    public List<DataImages> getDataImages() {
        return dataImages;
    }

    public void setDataImages(List<DataImages> dataImages) {
        this.dataImages = dataImages;
    }

    public DataToken getDataToken() {
        return dataToken;
    }

    public void setDataToken(DataToken dataToken) {
        this.dataToken = dataToken;
    }

    public DataVoting getDataVoting() {
        return dataVoting;
    }

    public void setDataVoting(DataVoting dataVoting) {
        this.dataVoting = dataVoting;
    }

    public DataProfile getDataProfile() {
        return dataProfile;
    }

    public void setDataProfile(DataProfile dataProfile) {
        this.dataProfile = dataProfile;
    }
}
