package com.hikiw.ecommerce.Enum;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    USER_NOT_EXISTED(1001, "User not exists.", HttpStatus.NOT_FOUND),
    USER_EXISTED(1002, "User existed.", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1003, "Unauthenticated.", HttpStatus.UNAUTHORIZED),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Error.", HttpStatus.INTERNAL_SERVER_ERROR), // dùng để trả về lỗi không xác định được
    CATEGORY_NOT_EXISTED(1004, "Category not exists.", HttpStatus.NOT_FOUND),
    PARENT_CATEGORY_NOT_EXISTED(1005, "Parent category not existed", HttpStatus.NOT_FOUND),
    INVALID_MOVE_TO_SELF(1006,"Invalid move to self",HttpStatus.BAD_REQUEST),
    CANNOT_MOVE_TO_CHILD(1007, "Can't move to chill", HttpStatus.BAD_REQUEST),
    SHOP_NOT_EXISTED(1008, "Shop not existed", HttpStatus.NOT_FOUND),
    DEFAULT_PICKUP_LOCATION_ALREADY_EXISTS(1009, "Default pickup location already exists for this shop", HttpStatus.BAD_REQUEST),
    SHOP_EXISTED(1010, "Shop existed", HttpStatus.BAD_REQUEST),
    SHOP_LOCATION_NOT_EXISTED(1011, "Shop location not existed", HttpStatus.NOT_FOUND)
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
