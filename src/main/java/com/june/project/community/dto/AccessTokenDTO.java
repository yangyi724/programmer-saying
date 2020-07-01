package com.june.project.community.dto;

import lombok.Data;

/**
 * @author June
 * @date 2020/6/30 - 15:21
 */
@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;

}
