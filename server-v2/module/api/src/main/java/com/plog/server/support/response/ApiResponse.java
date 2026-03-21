package com.plog.server.support.response;

import com.plog.server.support.exception.ErrorCode;

public record ApiResponse<T>(boolean success, T data, ErrorMessage error) {
    public static ApiResponse<Void> error(ErrorCode code, String message) {
        return new ApiResponse<>(false, null, new ErrorMessage(code, message));
    }
}
