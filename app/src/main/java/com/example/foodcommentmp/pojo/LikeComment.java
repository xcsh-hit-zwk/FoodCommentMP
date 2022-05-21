package com.example.foodcommentmp.pojo;

import java.util.Objects;

/**
 * @author: zhangweikun
 * @create: 2022-05-17 17:49
 */
public class LikeComment {

    private String commentId;
    private String username;
    private String restaurantName;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikeComment that = (LikeComment) o;
        return Objects.equals(commentId, that.commentId) && Objects.equals(username, that.username) && Objects.equals(restaurantName, that.restaurantName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, username, restaurantName);
    }
}
