package com.example.aureliengiudici.ethrade.NetworkManager;

/**
 * Created by aureliengiudici on 17/02/2018.
 */

import com.example.aureliengiudici.ethrade.Model.UserModel;

public class ServerResponse {
    private String result;
    private String message;
    private UserModel user;

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public UserModel getUser() {
        return user;
    }
}
