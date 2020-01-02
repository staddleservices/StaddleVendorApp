package staddlevendor.com.staddlevendor.ResponseClasses;

import java.util.ArrayList;

import staddlevendor.com.staddlevendor.bean.GetCategoryListModule;

public class GetCategoryListResponse {

    private String message;

    private String status;

    ArrayList<GetCategoryListModule> info;

    //================================

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

    public ArrayList<GetCategoryListModule> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<GetCategoryListModule> info) {
        this.info = info;
    }


   /* public Info[] getInfo ()
    {
        return info;
    }

    public void setInfo (Info[] info)
    {
        this.info = info;
    }*/


}
