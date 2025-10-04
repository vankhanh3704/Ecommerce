package com.hikiw.ecommerce.Enum;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USER_NOT_EXISTED(1001, "User not exists.", HttpStatus.NOT_FOUND),

    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Error.", HttpStatus.INTERNAL_SERVER_ERROR) // dùng để trả về lỗi không xác định được
    ;
    // mỗi errorCode có 3 thuộc tính
    private int code; // trả về mã lỗi
    private String message; // mô tả lỗi
    private HttpStatusCode httpStatusCode; // Mã HTTP tương ứng

    // constructor này để gán các giá trị cho enum
    ErrorCode(int code, String message, HttpStatusCode httpStatusCode){
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
