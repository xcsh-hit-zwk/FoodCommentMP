package com.example.foodcommentmp.pojo;

/**
 * @author: zhangweikun
 * @create: 2022-04-30 19:31
 */
public class RegisterAccount {

    private String username;
    private String password;
    private String nickname;

    public RegisterAccount() {
    }

    public RegisterAccount(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
