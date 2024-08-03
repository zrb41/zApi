package com.zrb.zapibackend.model.dto.userinterfaceinfo;

// [加入编程导航](https://www.code-nav.cn/) 入门捷径+交流答疑+项目实战+求职指导，帮你自学编程不走弯路

import lombok.Data;

import java.io.Serializable;

// 更新请求
@Data
public class UserInterfaceInfoUpdateRequest implements Serializable {

    private Long id;

    private Integer totalNum;

    private Integer leftNum;

    private Integer status;

    private static final long serialVersionUID = 1L;
}