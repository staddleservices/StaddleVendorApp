package staddlevendor.com.staddlevendor.ResponseClasses;

import java.util.ArrayList;

import staddlevendor.com.staddlevendor.bean.OnGoingOfferListModel;

public class OnGoingOfferListResponse {
    private String message;

    private String status;

    private ArrayList<OnGoingOfferListModel> info;

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

    public ArrayList<OnGoingOfferListModel> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<OnGoingOfferListModel> info) {
        this.info = info;
    }
}
