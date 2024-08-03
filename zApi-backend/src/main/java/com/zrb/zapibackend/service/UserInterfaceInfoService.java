package com.zrb.zapibackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zrb.zapicommon.model.entity.UserInterfaceInfo;

public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    // 接口调用次数+1，剩余次数-1
    boolean invokeCount(long interfaceInfoId, long userId);

    // 对于接口（interfaceInfoId），用户（userId）剩余的调用次数
    int leftNum(long interfaceInfoId, long userId);
}
