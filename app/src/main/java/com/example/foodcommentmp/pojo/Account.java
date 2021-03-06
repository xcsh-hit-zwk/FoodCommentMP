package com.example.foodcommentmp.pojo;

/**
 * @description: 对应后端Account实体类
 * @author: zhangweikun
 * @create: 2022-04-12 10:23
 **/
public class Account {

    private String username;
    private String password;

    public Account(){

    }


    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
