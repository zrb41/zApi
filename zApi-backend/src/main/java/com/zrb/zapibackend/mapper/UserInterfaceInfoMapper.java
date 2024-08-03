package com.zrb.zapibackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zrb.zapicommon.model.entity.UserInterfaceInfo;

import java.util.List;

public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    // 获取被调用次数最多前limit个接口信息
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}
