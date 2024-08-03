package com.zrb.zapibackend.model.dto.userinterfaceinfo;

import lombok.Data;
import java.io.Serializable;

// 创建请求
@Data
public class UserInterfaceInfoAddRequest implements Serializable {

    private Long userId;

    private Long interfaceInfoId;

    private Integer totalNum;

    private Integer leftNum;

}