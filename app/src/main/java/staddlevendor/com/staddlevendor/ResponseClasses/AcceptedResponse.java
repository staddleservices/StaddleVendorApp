package staddlevendor.com.staddlevendor.ResponseClasses;

import java.util.ArrayList;

import staddlevendor.com.staddlevendor.bean.AcceptedListModel;
import staddlevendor.com.staddlevendor.bean.PendingListModel;

public class AcceptedResponse {
    private String message;

    private String status;

    private ArrayList<AcceptedListModel> info;

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

    public ArrayList<AcceptedListModel> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<AcceptedListModel> info) {
        this.info = info;
    }
}
