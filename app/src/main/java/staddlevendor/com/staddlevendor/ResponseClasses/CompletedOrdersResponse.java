package staddlevendor.com.staddlevendor.ResponseClasses;

import java.util.ArrayList;

import staddlevendor.com.staddlevendor.bean.AcceptedListModel;
import staddlevendor.com.staddlevendor.bean.CompletedOrderList;

public class CompletedOrdersResponse {
    private String message;

    private String status;

    private ArrayList<CompletedOrderList> info;

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

    public ArrayList<CompletedOrderList> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<CompletedOrderList> info) {
        this.info = info;
    }
}
