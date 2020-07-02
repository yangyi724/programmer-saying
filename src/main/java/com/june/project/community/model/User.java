package com.june.project.community.model;

import lombok.Data;

/**
 * @author June
 * @date 2020/6/30 - 22:28
 */
@Data
public class
User {
    private int id;
    private String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;


}
