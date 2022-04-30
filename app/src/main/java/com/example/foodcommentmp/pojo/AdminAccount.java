package com.example.foodcommentmp.pojo;

/**
 * description: 管理员账户实体类
 * @author: zhangweikun
 * @create: 2022-04-30 8:35
 */
public class AdminAccount {
    private String username;
    private String password;

    public AdminAccount() {
    }

    public AdminAccount(String username, String password) {
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
