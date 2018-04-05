package com.example.aureliengiudici.ethrade.Model;

/**
 * Created by aureliengiudici on 05/04/2018.
 */

public class HistoryModel {
    private String date;
    private String address;
    private String addressOther;
    private boolean isSend;


    public HistoryModel(String date, String address, String addressOther, Boolean isSend) {
        this.date = date;
        this.address = address;
        this.addressOther = addressOther;
        this.isSend = isSend;
    }

    public String toString() {
        if (isSend) {
            return  " send to" + addressOther;
        } else {
            return  " receive from " + addressOther;
        }
    }

    public String getDate() {
        return this.date;
    }

    public String getAddress() {
        return this.address;
    }
}


