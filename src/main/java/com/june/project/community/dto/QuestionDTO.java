package com.june.project.community.dto;

import com.june.project.community.model.User;
import lombok.Data;

/**
 * @author June
 * @date 2020/7/1 - 20:08
 */
@Data
public class QuestionDTO {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}
