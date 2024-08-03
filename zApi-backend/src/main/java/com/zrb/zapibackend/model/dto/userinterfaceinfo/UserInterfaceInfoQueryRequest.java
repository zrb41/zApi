package com.zrb.zapibackend.model.dto.userinterfaceinfo;

import com.zrb.zapibackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

// 查询请求
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInterfaceInfoQueryRequest extends PageRequest implements Serializable {

    private Long id;

    private Long userId;

    private Long interfaceInfoId;

    private Integer totalNum;

    private Integer leftNum;

    private Integer status;

}