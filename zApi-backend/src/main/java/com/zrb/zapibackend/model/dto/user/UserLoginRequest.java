package com.zrb.zapibackend.model.dto.user;

import lombok.Data;
import java.io.Serializable;

// 用户登录请求
@Data
public class UserLoginRequest implements Serializable {

    private String userAccount;

    private String userPassword;

    private static final long serialVersionUID = 1L;
}
