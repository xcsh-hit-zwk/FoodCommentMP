package com.example.foodcommentmp.retrofit;

import com.example.foodcommentmp.pojo.CommentAddEntity;
import com.example.foodcommentmp.pojo.SearchInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author: zhangweikun
 * @create: 2022-05-19 17:41
 */

public interface CommentService {

    @POST("Comment/AddComment")
    Call<ResponseBody> addComment(@Body CommentAddEntity commentAddEntity);

    // searchWay = "GetComment" info = "%comment_id"
    @POST("Comment/GetComment")
    Call<ResponseBody> getComment(@Body SearchInfo searchInfo);

    // searchWay = "%comment_id" info = "%comment_info"
    @POST("Comment/ModifyComment")
    Call<ResponseBody> modifyComment(@Body SearchInfo searchInfo);

    // searchWay = "DeleteComment" info = "%comment_id"
    @POST("Comment/DeleteComment")
    Call<ResponseBody> deleteComment(@Body SearchInfo searchInfo);
}
