package com.june.project.community.enums;

/**
 * @author June
 * @date 2020/7/4 - 18:44
 * 记录comment的父类是问题还是评论
 */
public enum CommentTypeEnum {

    QUESTION(1),
    COMMENT(2);

    public static boolean isExist(Integer type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if(commentTypeEnum.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }

    private Integer type;

    CommentTypeEnum(Integer type) {
        this.type = type;
    }
}
