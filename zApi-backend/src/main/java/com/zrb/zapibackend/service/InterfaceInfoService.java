package com.zrb.zapibackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zrb.zapicommon.model.entity.InterfaceInfo;

public interface InterfaceInfoService extends IService<InterfaceInfo> {
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
