package com.example.foodcommentmp.pojo;

/**
 * @author: zhangweikun
 * @create: 2022-05-15 9:02
 */
public class SearchInfo {

    private String searchWay;
    private String info;

    public SearchInfo() {
    }

    public SearchInfo(String searchWay, String info) {
        this.searchWay = searchWay;
        this.info = info;
    }

    public String getSearchWay() {
        return searchWay;
    }

    public void setSearchWay(String searchWay) {
        this.searchWay = searchWay;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
