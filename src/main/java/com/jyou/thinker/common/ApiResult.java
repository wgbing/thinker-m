package com.jyou.thinker.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * TODO: 接口返回结果封装
 * @author wgbing
 * @date 2019-09-29 18:11
 */
@Data
public class ApiResult {

    private int code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Object data;

    public static ApiResult success() {
        return new ApiResult(Constant.OPERATE_SUCCESS, Constant.OPERATE_SUCCESS_STR, null);
    }

    public static ApiResult success(String message) {
        return new ApiResult(Constant.OPERATE_SUCCESS, message, null);
    }

    public static ApiResult success(Object data) {
        return new ApiResult(Constant.OPERATE_SUCCESS, Constant.OPERATE_SUCCESS_STR, data);
    }

    public static ApiResult success(String message, Object data) {
        return new ApiResult(Constant.OPERATE_SUCCESS, message, data);
    }

    public static ApiResult failure() {
        return new ApiResult(Constant.OPERATE_ERROR, Constant.OPERATE_ERROR_STR, null);
    }

    public static ApiResult failure(String message) {
        return new ApiResult(Constant.OPERATE_ERROR, message, null);
    }

    public static ApiResult failure(Object data) {
        return new ApiResult(Constant.OPERATE_ERROR, Constant.OPERATE_ERROR_STR, data);
    }

    public static ApiResult failure(String message, Object data) {
        return new ApiResult(Constant.OPERATE_ERROR, message, data);
    }

    public ApiResult() {
    }

    private ApiResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return this.code == Constant.OPERATE_SUCCESS;
    }
}
