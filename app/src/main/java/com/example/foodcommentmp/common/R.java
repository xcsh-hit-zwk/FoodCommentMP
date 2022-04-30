package com.example.foodcommentmp.common;

import com.example.foodcommentmp.enums.ResultCode;

/**
 * @author: zhangweikun
 * @create: 2022-04-30 10:34
 */
public class R {

    private Boolean success;

    private Integer code;

    private String message;

    private Object data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    // 构造器私有
    private R() {
    }

    // 通用返回成功
    public static R ok() {
        R r = new R();
        r.setSuccess(ResultCode.SUCCESS.getSuccess());
        r.setCode(ResultCode.SUCCESS.getCode());
        r.setMessage(ResultCode.SUCCESS.getMessage());
        return r;
    }

    // 通用返回失败，未知错误
    public static R error() {
        R r = new R();
        r.setSuccess(ResultCode.UNKNOWN_ERROR.getSuccess());
        r.setCode(ResultCode.UNKNOWN_ERROR.getCode());
        r.setMessage(ResultCode.UNKNOWN_ERROR.getMessage());
        return r;
    }

    // 设置结果，形参为结果枚举
    public static R setResult(ResultCode result) {
        R r = new R();
        r.setSuccess(result.getSuccess());
        r.setCode(result.getCode());
        r.setMessage(result.getMessage());
        return r;
    }

    /**
     * ------------使用链式编程，返回类本身-----------
     **/

    // 自定义返回数据
    public R data(Object object) {
        this.setData(object);
        return this;
    }

    // 自定义状态信息
    public R message(String message) {
        this.setMessage(message);
        return this;
    }

    // 自定义状态码
    public R code(Integer code) {
        this.setCode(code);
        return this;
    }
}
