package staddlevendor.com.staddlevendor.ResponseClasses;

import java.util.ArrayList;

import staddlevendor.com.staddlevendor.bean.GetCategoryListModule;
import staddlevendor.com.staddlevendor.bean.GetSubCategoryListModule;

public class GetSubCategoryListResponse {

    private String message;

    private String status;

    ArrayList<GetSubCategoryListModule> info;

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

    public ArrayList<GetSubCategoryListModule> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<GetSubCategoryListModule> info) {
        this.info = info;
    }
}
