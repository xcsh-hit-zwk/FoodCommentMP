package com.example.foodcommentmp.pojo;

/**
 * @author: zhangweikun
 * @create: 2022-05-17 17:45
 */
public class CommentLiked {

    private String usercommentlikeId;
    private String commentId;
    private String userId;
    private String restaurantId;

    public String getUsercommentlikeId() {
        return usercommentlikeId;
    }

    public void setUsercommentlikeId(String usercommentlikeId) {
        this.usercommentlikeId = usercommentlikeId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
