package com.june.project.community.dto;

import lombok.Data;

/**
 * @author June
 * @date 2020/6/30 - 16:51
 */
@Data
public class GithubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatarUrl;
}
