package com.june.project.community.model;

import lombok.Data;

/**
 * @author June
 * @date 2020/7/1 - 13:50
 */
@Data
public class Question {
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

}
