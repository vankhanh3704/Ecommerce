package com.hikiw.ecommerce.Exception;


import com.hikiw.ecommerce.Enum.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// AppException kế thừa lớp cha để bắt lỗi cho hợp lý
public class AppException extends RuntimeException{
    private ErrorCode errorCode;

    public AppException(ErrorCode errorCode){
        // truyền message của errorCode lên lớp cha (RuntimeException) để lấy thông tin lỗi chuẩn
        super(errorCode.getMessage());

        this.errorCode = errorCode;
    }
}
