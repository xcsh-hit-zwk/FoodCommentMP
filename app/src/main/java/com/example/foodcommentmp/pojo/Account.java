package com.example.foodcommentmp.pojo;

/**
 * @description: 对应后端Account实体类
 * @author: zhangweikun
 * @create: 2022-04-12 10:23
 **/
public class Account {

    private String userId;
    private String password;
    private Boolean hasDelete;

    public Account(){

    }

    public Account(String userId, String password, Boolean hasDelete){
        this.userId = userId;
        this.password = password;
        this.hasDelete = hasDelete;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
