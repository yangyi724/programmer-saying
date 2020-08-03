package com.june.project.community.dto;

import com.june.project.community.model.User;
import lombok.Data;

/**
 * @author 延君
 * @date 2020/8/2 - 20:12
 */
@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private Integer commentCount;
    private String content;
    private User user;

}
