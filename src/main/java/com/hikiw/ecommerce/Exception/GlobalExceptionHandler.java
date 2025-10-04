package com.hikiw.ecommerce.Exception;


import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.Model.Response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// ở đây mình sẽ định nghĩa các Exception
@ControllerAdvice // bắt và xử lý lỗi toàn cục trong toàn bộ ứng dụng Spring (không cần try-catch ở từng controller).
@Slf4j
public class GlobalExceptionHandler {

    // define(định nghĩa) cho RuntimeException
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handingRuntimeException(RuntimeException e){
        log.error("Unexpected exception: ", e);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity
                .status(ErrorCode.UNCATEGORIZED_EXCEPTION.getHttpStatusCode())
                .body(apiResponse);
    }

    // define cho AppException
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handingAppException(AppException e){
        ApiResponse apiResponse = new ApiResponse();
        ErrorCode errorCode = e.getErrorCode();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(apiResponse);
    }
}
