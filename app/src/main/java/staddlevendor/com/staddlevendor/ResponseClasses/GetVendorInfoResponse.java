package staddlevendor.com.staddlevendor.ResponseClasses;

import java.util.ArrayList;

import staddlevendor.com.staddlevendor.bean.GetVendorInfoModel;

public class GetVendorInfoResponse {
    private String message;

    private String status;

    private ArrayList<GetVendorInfoModel> info;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<GetVendorInfoModel> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<GetVendorInfoModel> info) {
        this.info = info;
    }
}
