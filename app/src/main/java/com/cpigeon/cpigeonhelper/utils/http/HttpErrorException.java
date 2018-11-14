package com.cpigeon.cpigeonhelper.utils.http;


import com.cpigeon.cpigeonhelper.common.network.ApiResponse;

public class HttpErrorException extends RuntimeException {
    private ApiResponse apiResponse;
    public HttpErrorException(ApiResponse ApiResponse) {
        super(ApiResponse!=null?ApiResponse.msg:"");
        this.apiResponse=ApiResponse;
    }

    public ApiResponse getResponseJson() {
        return apiResponse;
    }
}