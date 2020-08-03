package com.june.project.community.dto;

import lombok.Data;

import java.util.List;

/**
 * @author 延君
 * @date 2020/8/3 - 20:20
 */
@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}
