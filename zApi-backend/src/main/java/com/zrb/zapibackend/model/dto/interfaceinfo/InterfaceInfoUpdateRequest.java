package com.zrb.zapibackend.model.dto.interfaceinfo;

import lombok.Data;
import java.io.Serializable;

// 更新请求
@Data
public class InterfaceInfoUpdateRequest implements Serializable {

    private Long id;

    private String name;

    private String description;

    private String url;

    private String requestParams;

    private String requestHeader;

    private String responseHeader;

    private Integer status;

    private String method;

    private static final long serialVersionUID = 1L;
}