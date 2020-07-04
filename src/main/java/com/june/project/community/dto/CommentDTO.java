package com.june.project.community.dto;

import lombok.Data;

/**
 * @author June
 * @date 2020/7/4 - 17:23
 */
@Data
public class CommentDTO {
    private Long parentId;
    private String content;
    private Integer type;
}
