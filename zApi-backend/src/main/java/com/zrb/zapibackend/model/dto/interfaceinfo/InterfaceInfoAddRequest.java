package com.zrb.zapibackend.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

// 接口创建请求
@Data
public class InterfaceInfoAddRequest implements Serializable {

    private String name;

    private String description;

    private String url;

    private String requestParams;

    private String requestHeader;

    private String responseHeader;

    private String method;

}