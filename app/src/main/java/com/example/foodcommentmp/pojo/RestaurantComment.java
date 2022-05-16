package com.example.foodcommentmp.pojo;

/**
 * @author: zhangweikun
 * @create: 2022-05-16 16:35
 */
public class RestaurantComment {

    private String username;
    private String userImage;
    private String nickname;
    private String commentInfo;
    private String commentLike;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCommentInfo() {
        return commentInfo;
    }

    public void setCommentInfo(String commentInfo) {
        this.commentInfo = commentInfo;
    }

    public String getCommentLike() {
        return commentLike;
    }

    public void setCommentLike(String commentLike) {
        this.commentLike = commentLike;
    }
}
