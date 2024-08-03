package com.zrb.zapibackend.model.dto.interfaceinfo;

import com.zrb.zapibackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

// 查询请求
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {

    private Long id;

    private String name;

    private String description;

    private String url;

    private String requestHeader;

    private String responseHeader;

    private Integer status;

    private String method;

    private Long userId;

}