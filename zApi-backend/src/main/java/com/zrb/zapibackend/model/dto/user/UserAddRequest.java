package com.zrb.zapibackend.model.dto.user;

import lombok.Data;
import java.io.Serializable;

// 用户创建请求
@Data
public class UserAddRequest implements Serializable {

    private String userName;

    private String userAccount;

    private String userAvatar;

    private Integer gender;

    private String userRole;

    private String userPassword;

    private static final long serialVersionUID = 1L;
}