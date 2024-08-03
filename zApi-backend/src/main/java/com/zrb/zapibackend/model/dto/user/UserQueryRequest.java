package com.zrb.zapibackend.model.dto.user;


import com.zrb.zapibackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;

// 用户查询请求
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {

    private Long id;

    private String userName;

    private String userAccount;

    private String userAvatar;

    private Integer gender;

    private String userRole;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}