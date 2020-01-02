package staddlevendor.com.staddlevendor.ResponseClasses;

import java.util.ArrayList;

import staddlevendor.com.staddlevendor.bean.LoginInfoModel;

public class LoginResponse {

    private String message;

    private String status;

    private LoginInfoModel info;

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

    public LoginInfoModel getInfo() {
        return info;
    }

    public void setInfo(LoginInfoModel info) {
        this.info = info;
    }
}
