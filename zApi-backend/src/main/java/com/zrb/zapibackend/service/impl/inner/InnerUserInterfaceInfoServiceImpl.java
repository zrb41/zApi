package com.zrb.zapibackend.service.impl.inner;

import com.zrb.zapibackend.service.UserInterfaceInfoService;
import com.zrb.zapicommon.service.InnerUserInterfaceInfoService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId,userId);
    }

    @Override
    public boolean isInvoke(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.leftNum(interfaceInfoId,userId)>0;
    }
}
