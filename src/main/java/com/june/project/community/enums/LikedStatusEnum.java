package com.june.project.community.enums;

/**
 * @author 延君
 * @date 2020/8/6 - 17:02
 */
public enum LikedStatusEnum {
    LIKE(1),
    UNLIKE(0);
    private int status;

    public int getStatus() {
        return status;
    }

    LikedStatusEnum(int status) {
        this.status = status;
    }
}
