package com.example.aureliengiudici.ethrade.Model;

/**
 * Created by aureliengiudici on 17/02/2018.
 */

public class UserModel {
        private String name;
        private String email;
        private String password;
        private String address;
        private String type;
        private Boolean isAContact;


        public UserModel(String name, String email, String address, Boolean isAContact) {
            this.name = name;
            this.email = email;
            this.address = address;
            this.isAContact = isAContact;
            this.generateType();
        }

    public UserModel(String name, String address, Boolean isAContact) {
        this.name = name;
        this.address = address;
        this.isAContact = isAContact;
        this.type = this.generateType();
    }

    public UserModel(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserModel(String name, String password) {
            this.name = name;
            this.password = password;
    }


    private String generateType() {
       return (isAContact  ? "contact" : "not a contact");
    }

    public String getType() {
            return type;
    }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

    public String getAddress() {
        return  address;
    }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setEmail(String email) {
            this.email = email;
        }
}
