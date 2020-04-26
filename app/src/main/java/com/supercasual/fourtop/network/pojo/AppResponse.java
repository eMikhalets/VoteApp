package com.supercasual.fourtop.network.pojo;

import java.util.List;

public class AppResponse {

    private String dataString;
    private List<DataImages> dataImages;
    private DataToken dataToken;
    private DataVoting dataVoting;

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
}
