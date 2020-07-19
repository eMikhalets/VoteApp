package com.ntech.fourtop.network.pojo;

import java.util.List;

public class AppResponse {

    private String dataString;
    private List<DataImage> dataImages;
    private DataToken dataToken;
    private DataVoting dataVoting;
    private DataProfile dataProfile;

    public AppResponse() {
    }

    public AppResponse(String dataString) {
        this.dataString = dataString;
    }

    public AppResponse(List<DataImage> dataImages) {
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

    public String getDataString() {
        return dataString;
    }

    public List<DataImage> getDataImages() {
        return dataImages;
    }

    public DataToken getDataToken() {
        return dataToken;
    }

    public DataVoting getDataVoting() {
        return dataVoting;
    }

    public DataProfile getDataProfile() {
        return dataProfile;
    }
}
