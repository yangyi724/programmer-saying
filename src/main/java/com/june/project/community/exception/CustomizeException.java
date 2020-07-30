package com.june.project.community.exception;

/**
 * @author June
 * @date 2020/7/2 - 23:14
 */
public class CustomizeException extends RuntimeException{ // 定义成runtime不会影响其他代码，只要在CutomizeExceptionHandler进行拦截就可以了
    private String message;
    private Integer code;

    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}




