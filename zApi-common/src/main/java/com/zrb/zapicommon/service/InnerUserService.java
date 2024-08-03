package com.zrb.zapicommon.service;

import com.zrb.zapicommon.model.entity.User;

// 内部用户服务
public interface InnerUserService {

    // 根据accessKey返回用户信息
    User getInvokeUser(String accessKey);
}
