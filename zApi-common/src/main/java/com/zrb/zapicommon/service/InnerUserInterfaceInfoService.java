package com.zrb.zapicommon.service;

// 内部用户接口信息服务

public interface InnerUserInterfaceInfoService {

    // 接口调用次数+1，剩余次数-1
    boolean invokeCount(long interfaceInfoId, long userId);

    // 对于接口interfaceInfoId，用户userId是否还有剩余调用次数
    boolean isInvoke(long interfaceInfoId, long userId);


}
