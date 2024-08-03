package com.zrb.zapibackend.model.dto.user;

import lombok.Data;
import java.io.Serializable;

// 户注册请求
@Data
public class UserRegisterRequest implements Serializable {

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private static final long serialVersionUID = 1L;
}
