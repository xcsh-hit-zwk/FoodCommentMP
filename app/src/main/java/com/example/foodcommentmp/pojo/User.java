package com.example.foodcommentmp.pojo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description: 对应后端User实体类
 * @author: zhangweikun
 * @create: 2022-04-12 10:23
 **/
public class User {

    private String id;

    private String userId;
    private String password;
    private String nickname;
    private String createTime;
    private String modTime;
    private Boolean hasLogin;
    private Boolean hasDelete;

    public User(){

    }

    public User(String userId, String password, String nickname){
        Date date = new Date();
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.id = "";
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        createTime = simpleDateFormat.format(date);
        modTime = createTime;
        hasLogin = false;
        hasDelete = false;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModTime() {
        return modTime;
    }

    public void setModTime(String modTime) {
        this.modTime = modTime;
    }

    public Boolean getHasLogin() {
        return hasLogin;
    }

    public void setHasLogin(Boolean hasLogin) {
        this.hasLogin = hasLogin;
    }

    public Boolean getHasDelete() {
        return hasDelete;
    }

    public void setHasDelete(Boolean hasDelete) {
        this.hasDelete = hasDelete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
