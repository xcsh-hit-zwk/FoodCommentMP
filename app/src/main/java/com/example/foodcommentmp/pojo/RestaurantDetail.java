package com.example.foodcommentmp.pojo;

import java.util.List;

/**
 * @author: zhangweikun
 * @create: 2022-05-16 16:27
 */
public class RestaurantDetail {

    private String restaurantId;
    private String restaurantName;
    private Integer restaurantLikes;
    private String restaurantTag;
    private String restaurantBlock;
    private String restaurantPosition;
    private List<FoodOverView> foodList;
    private List<String> labelList;
    private List<RestaurantComment> commentList;
    // 只要6个
    private List<RestaurantOverView> sameTagList;
    private List<CommentLiked> likedCommentList;
    private List<FoodLiked> likedFoodList;

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Integer getRestaurantLikes() {
        return restaurantLikes;
    }

    public void setRestaurantLikes(Integer restaurantLikes) {
        this.restaurantLikes = restaurantLikes;
    }

    public String getRestaurantTag() {
        return restaurantTag;
    }

    public void setRestaurantTag(String restaurantTag) {
        this.restaurantTag = restaurantTag;
    }

    public String getRestaurantBlock() {
        return restaurantBlock;
    }

    public void setRestaurantBlock(String restaurantBlock) {
        this.restaurantBlock = restaurantBlock;
    }

    public String getRestaurantPosition() {
        return restaurantPosition;
    }

    public void setRestaurantPosition(String restaurantPosition) {
        this.restaurantPosition = restaurantPosition;
    }

    public List<FoodOverView> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<FoodOverView> foodList) {
        this.foodList = foodList;
    }

    public List<String> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<String> labelList) {
        this.labelList = labelList;
    }

    public List<RestaurantComment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<RestaurantComment> commentList) {
        this.commentList = commentList;
    }

    public List<RestaurantOverView> getSameTagList() {
        return sameTagList;
    }

    public void setSameTagList(List<RestaurantOverView> sameTagList) {
        this.sameTagList = sameTagList;
    }

    public List<CommentLiked> getLikedCommentList() {
        return likedCommentList;
    }

    public void setLikedCommentList(List<CommentLiked> likedCommentList) {
        this.likedCommentList = likedCommentList;
    }

    public List<FoodLiked> getLikedFoodList() {
        return likedFoodList;
    }

    public void setLikedFoodList(List<FoodLiked> likedFoodList) {
        this.likedFoodList = likedFoodList;
    }
}
