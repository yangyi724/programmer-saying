package com.june.project.community.dto;

import lombok.Data;


/**
 * @author 延君
 * @date 2020/8/7 - 17:17
 */
@Data
public class LikeAndCommentDTO {
    private CommentDTO commentDTO;
    private int likeCount;
}
