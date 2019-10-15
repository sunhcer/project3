package com.stylefeng.guns.rest.user.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegister implements Serializable {

    private static final long serialVersionUID = -6269774824328006993L;
    String username;
    String password;
    String mobile;
    String  email;
    String address;
}
