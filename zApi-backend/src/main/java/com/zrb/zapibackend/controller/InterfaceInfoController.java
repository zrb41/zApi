package com.zrb.zapibackend.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zrb.zapibackend.annotation.AuthCheck;
import com.zrb.zapibackend.common.*;
import com.zrb.zapibackend.constant.CommonConstant;
import com.zrb.zapibackend.exception.BusinessException;
import com.zrb.zapibackend.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.zrb.zapicommon.model.dto.InterfaceInfoInvokeRequest;
import com.zrb.zapibackend.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.zrb.zapibackend.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.zrb.zapibackend.model.enums.InterfaceInfoStatusEnum;
import com.zrb.zapibackend.service.InterfaceInfoService;
import com.zrb.zapibackend.service.UserService;
import com.zrb.zapicommon.model.entity.InterfaceInfo;
import com.zrb.zapicommon.model.entity.User;
import com.zrb.zapisdk.ZApiClient;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("interfaceInfo")
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;


    // 添加接口
    @PostMapping("add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request){
        if(interfaceInfoAddRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtil.copyProperties(interfaceInfoAddRequest,interfaceInfo);
        // 校验接口
        interfaceInfoService.validInterfaceInfo(interfaceInfo,true);
        // 获取当前登录用户，设置接口的创建人
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        // 不走业务层，直接插入
        boolean result = interfaceInfoService.save(interfaceInfo);
        if(!result){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        // 返回新建接口id
        Long id = interfaceInfo.getId();
        return ResultUtils.success(id);
    }

    // 删除接口
    @PostMapping("delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest,HttpServletRequest request){
        if(deleteRequest==null||deleteRequest.getId()<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取当前登录用户
        User user = userService.getLoginUser(request);
        // 判断接口是否存在
        Long interfaceId = deleteRequest.getId();
        InterfaceInfo delInterface = interfaceInfoService.getById(interfaceId);
        if(delInterface==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 如果不是本人并且也不是管理员，则不可以删除
        if(!delInterface.getUserId().equals(user.getId())&&!userService.isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 删除，不走业务层
        boolean result = interfaceInfoService.removeById(interfaceId);
        // 返回
        return ResultUtils.success(result);
    }

    @PostMapping("update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest,
                                                     HttpServletRequest request){
        if(interfaceInfoUpdateRequest==null||interfaceInfoUpdateRequest.getId()<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtil.copyProperties(interfaceInfoUpdateRequest,interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo,false);
        // 获取当前登录用户
        User user = userService.getLoginUser(request);
        // 判断接口是否存在
        Long interfaceId = interfaceInfoUpdateRequest.getId();
        InterfaceInfo updateInterface = interfaceInfoService.getById(interfaceId);
        if(updateInterface==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可以修改
        if(!updateInterface.getUserId().equals(user.getId())&&!userService.isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 更新，不走业务层
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    // 根据id获取接口
    @GetMapping("get")
    public BaseResponse<InterfaceInfo> getInterfaceById(long id){
        if(id<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        return ResultUtils.success(interfaceInfo);
    }

    // 获取列表，仅管理员可用
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        if (interfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfo);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfo);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        return ResultUtils.success(interfaceInfoList);
    }

    // 分页
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String description = interfaceInfoQuery.getDescription();
        // description 需支持模糊搜索
        interfaceInfoQuery.setDescription(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(interfaceInfoPage);
    }

    // 接口上线
    @PostMapping("online")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest,HttpServletRequest request){
        // 参数校验
        if(idRequest==null||idRequest.getId()<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断接口是否存在
        Long id = idRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if(oldInterfaceInfo==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 判断接口是否可用 // todo


        // 仅管理员可修改
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        // 不走业务层，直接修改
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    // 接口下线
    @PostMapping("offline")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest,HttpServletRequest request){
        // 参数校验
        if(idRequest==null||idRequest.getId()<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断接口是否存在
        Long id = idRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if(oldInterfaceInfo==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅管理员可修改
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        // 不走业务层，直接修改
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    // 接口的调用
    @PostMapping("invoke")
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest,HttpServletRequest request){
        // 参数校验
        if(interfaceInfoInvokeRequest==null||interfaceInfoInvokeRequest.getId()<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取接口id
        Long interfaceInfoId = interfaceInfoInvokeRequest.getId();
        // 获取用户请求参数
        String userRequestParams = interfaceInfoInvokeRequest.getUserRequestParams();
        // 判断接口是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(interfaceInfoId);
        if(oldInterfaceInfo==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 判断接口是否可用
        if(oldInterfaceInfo.getStatus()==InterfaceInfoStatusEnum.OFFLINE.getValue()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口已关闭");
        }
        // 获取当前登录用户
        User user = userService.getLoginUser(request);
        String accessKey = user.getAccessKey();
        String secretKey = user.getSecretKey();
        // 创建sdk客户端
        ZApiClient zApiClient = new ZApiClient(accessKey, secretKey);
        // 将请求体转化为Json字符串
        String body = JSONUtil.toJsonStr(interfaceInfoInvokeRequest);
        String result = zApiClient.redirect(interfaceInfoId,userRequestParams);
        return ResultUtils.success(result);
    }

}
