package staddlevendor.com.staddlevendor.ResponseClasses;


import java.util.ArrayList;

import staddlevendor.com.staddlevendor.bean.PendingListModel;
import staddlevendor.com.staddlevendor.bean.ProductListModel;

public class ProductListResponse {
    private String message;

    private String status;

    private ArrayList<PendingListModel> info;

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

    public ArrayList<PendingListModel> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<PendingListModel> info) {
        this.info = info;
    }
}

