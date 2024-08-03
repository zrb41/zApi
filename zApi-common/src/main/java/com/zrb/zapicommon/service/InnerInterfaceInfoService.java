package com.zrb.zapicommon.service;

import com.zrb.zapicommon.model.entity.InterfaceInfo;

// 内部接口信息服务
public interface InnerInterfaceInfoService {

    // 获取接口信息
    InterfaceInfo getInterfaceInfo(String url, String method);
}
