package com.zrb.zapibackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zrb.zapibackend.common.ErrorCode;
import com.zrb.zapibackend.exception.BusinessException;
import com.zrb.zapibackend.mapper.UserInterfaceInfoMapper;
import com.zrb.zapibackend.service.UserInterfaceInfoService;
import com.zrb.zapicommon.model.entity.UserInterfaceInfo;
import org.springframework.stereotype.Service;

@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo> implements UserInterfaceInfoService {

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        // 参数校验
        if(interfaceInfoId<=0||userId<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceInfoId",interfaceInfoId);
        updateWrapper.eq("userId",userId);
        updateWrapper.setSql("leftNum=leftNum-1, totalNum=totalNum+1");
        // 更新
        return update(updateWrapper);
    }

    @Override
    public int leftNum(long interfaceInfoId, long userId) {
        // 参数校验
        if(interfaceInfoId<=0||userId<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interfaceInfoId",interfaceInfoId);
        queryWrapper.eq("userId",userId);
        UserInterfaceInfo userInterfaceInfo = getOne(queryWrapper);
        return userInterfaceInfo.getLeftNum();
    }
}
