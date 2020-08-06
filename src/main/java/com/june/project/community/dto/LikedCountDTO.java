package com.june.project.community.dto;

import lombok.Data;

/**
 * @author 延君
 * @date 2020/8/6 - 16:12
 */
@Data
public class LikedCountDTO {
    // 被点赞的回复的id
    private Long commentId;
    // 被点赞的回复的总点赞量
    private Integer likeCount;
}
