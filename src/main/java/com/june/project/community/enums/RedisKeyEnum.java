package com.june.project.community.enums;

/**
 * @author 延君
 * @date 2020/8/6 - 16:33
 */
public enum RedisKeyEnum {
    QUESTION_VIEW_COUNT_KEY("question_view"),
    COMMENT_LIKE_INFO_KEY("question_like_info"),
    COMMENT_LIKE_COUNT_KEY("question_like_count"),
    QUESTION_VIEW_COUNT_CODE("viewcount_"),
    COMMENT_LIKE_INFO_CODE("likeinfo_"),
    COMMENT_LIKE_COUNT_CODE("likecount_");
    private String key;

    public String getKey() {
        return key;
    }

    RedisKeyEnum(String key) {
        this.key = key;
    }
}
