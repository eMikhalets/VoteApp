package com.ntech.fourtop.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseImages {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("error_code")
    @Expose
    private int errorCode;
    @SerializedName("error_msg")
    @Expose
    private String errorMsg;
    @SerializedName("error_ucode")
    @Expose
    private String errorUcode;
    @SerializedName("data")
    @Expose
    private List<DataImage> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorUcode() {
        return errorUcode;
    }

    public void setErrorUcode(String errorUcode) {
        this.errorUcode = errorUcode;
    }

    public List<DataImage> getData() {
        return data;
    }

    public void setData(List<DataImage> data) {
        this.data = data;
    }
}
