package com.june.project.community.exception;

/**
 * @author June
 * @date 2020/7/2 - 23:44
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode{


    QUESTION_NOT_FOUND(2001, "你找到问题好像不存在，要不换个试试？"),
    TARAGET_PARAM_NOT_FOUND(2002, "未选中任何问题或评论进行回复"),
    NO_LOGIN(2003, "当前操作需要登录，请登录后重试"),
    SYSTEM_ERROR(2004, "服务冒烟了，要不然你稍后再试试！！"),
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006, "回复的评论不存在"),
    CONTENT_IS_EMPTY(2007, "输入内容不能为空"),
    NOTIFICATION_NOT_FOUND(2008, "这条通知好像搞丢了"),
    READ_NOTIFICATION_FAIL(2009, "不能读取别人的信息哦")
    ;
    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

}
