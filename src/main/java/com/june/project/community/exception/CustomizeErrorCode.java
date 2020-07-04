package com.june.project.community.exception;

/**
 * @author June
 * @date 2020/7/2 - 23:44
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode{


    QUESTION_NOT_FOUND("你找到问题好像不存在，要不换个试试？");
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    CustomizeErrorCode(String message) {
        this.message = message;
    }


}
