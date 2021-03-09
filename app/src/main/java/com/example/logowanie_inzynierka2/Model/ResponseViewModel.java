package com.example.logowanie_inzynierka2.Model;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class ResponseViewModel {

    private String message;
    private boolean isSuccess;




    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        message = message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }





    public ResponseViewModel(String message, boolean isSuccess){
        this.isSuccess=isSuccess;
        this.message=message;
    }





}
