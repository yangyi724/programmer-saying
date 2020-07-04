package com.june.project.community.exception;

/**
 * @author June
 * @date 2020/7/2 - 23:14
 */
public class CustomizeException extends RuntimeException{ // 定义成runtime不会影响其他代码，只要在CutomizeExceptionHandler进行拦截就可以了
    private String message;

    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.message = errorCode.getMessage();
    }

    public CustomizeException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}




