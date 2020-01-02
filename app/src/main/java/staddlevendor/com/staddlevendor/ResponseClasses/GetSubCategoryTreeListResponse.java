package staddlevendor.com.staddlevendor.ResponseClasses;

import java.util.ArrayList;

import staddlevendor.com.staddlevendor.bean.GetSubCategoryListModule;
import staddlevendor.com.staddlevendor.bean.GetSubCategoryTreeListModule;

public class GetSubCategoryTreeListResponse {

    private String message;

    private String status;

    private ArrayList<GetSubCategoryTreeListModule> info;

    //==============================================
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

    public ArrayList<GetSubCategoryTreeListModule> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<GetSubCategoryTreeListModule> info) {
        this.info = info;
    }
}
