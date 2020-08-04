package com.june.project.community.dto;

import lombok.Data;

/**
 * @author 延君
 * @date 2020/8/4 - 9:44
 */
@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;
    private String notifierName;
    private Long outerid;
    private String outerTitle;
    private Integer type;
    private String typeName;
}
