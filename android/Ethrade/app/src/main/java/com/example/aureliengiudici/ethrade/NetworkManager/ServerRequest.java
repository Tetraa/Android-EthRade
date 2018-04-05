package com.example.aureliengiudici.ethrade.NetworkManager;

import com.example.aureliengiudici.ethrade.Model.UserModel;

/**
 * Created by aureliengiudici on 17/02/2018.
 */

public class ServerRequest {
    private String operation;
    private UserModel user;

    public ServerRequest(String operation, UserModel user) {
        this.operation = operation;
        this.user = user;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
