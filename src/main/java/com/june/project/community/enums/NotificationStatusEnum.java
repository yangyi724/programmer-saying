package com.june.project.community.enums;

/**
 * @author 延君
 * @date 2020/8/4 - 10:03
 */
public enum NotificationStatusEnum {
    UNREAD(0),
    READ(1);

    private int status;

    public int getStatus() {
        return status;
    }

    NotificationStatusEnum(int status) {
        this.status = status;
    }
}
