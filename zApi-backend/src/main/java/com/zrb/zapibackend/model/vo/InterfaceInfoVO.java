package com.zrb.zapibackend.model.vo;

import com.zrb.zapicommon.model.entity.InterfaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

// 接口视图
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoVO extends InterfaceInfo {

    private Integer totalNum;

    private static final long serialVersionUID = 1L;
}