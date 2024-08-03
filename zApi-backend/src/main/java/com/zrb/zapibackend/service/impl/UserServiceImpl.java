package com.zrb.zapibackend.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zrb.zapibackend.common.ErrorCode;
import com.zrb.zapibackend.constant.UserConstant;
import com.zrb.zapibackend.exception.BusinessException;
import com.zrb.zapibackend.mapper.UserMapper;
import com.zrb.zapibackend.service.UserService;
import com.zrb.zapicommon.model.entity.User;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    // 盐值
    private final String SALT = "zrb41";

    // 用户注册
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 参数校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        if (userAccount.length() < UserConstant.MIN_USER_ACCOUNT_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        int minPasswordLen = UserConstant.MIN_PASSWORD_LEN;
        if (userPassword.length() < minPasswordLen || checkPassword.length() < minPasswordLen) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }

        // 上锁 - 防止多个线程重复注册
        synchronized (userAccount.intern()) {

            // 校验账号是否存在
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            Long count = userMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 加密密码
            String encryptPassword = DigestUtil.md5Hex(SALT + userPassword);
            // 分配 accessKey 和 secretKey
            String accessKey = DigestUtil.md5Hex(SALT + userAccount + RandomUtil.randomString(5));
            String secretKey = DigestUtil.md5Hex(SALT + userAccount + RandomUtil.randomString(8));
            // 插入数据
            User user = User.builder().userAccount(userAccount).userPassword(encryptPassword)
                    .accessKey(accessKey).secretKey(secretKey).build();
            boolean isSave = save(user);
            if (!isSave) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            // 返回新增用户id
            return user.getId();
        }

    }

    // 用户登录
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        // 参数校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        if (userAccount.length() < UserConstant.MIN_USER_ACCOUNT_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < UserConstant.MIN_PASSWORD_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }

        // 加密密码
        String encryptPassword = DigestUtil.md5Hex(SALT + userPassword);
        // 查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = getOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 在session中存储用户信息
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);
        // 返回用户
        return user;
    }

    // 获取当前登录用户
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 从session中获取用户信息
        User user = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        // 判断是否登录
        if (user == null || user.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "未登录错误");
        }
        // 从数据库中获取最新的用户信息
        // 如果追求效率，也可以走缓存（Redis）
        user = getById(user.getId());
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "未登录错误");
        }
        return user;
    }

    // 判断当前登录用户是否是管理员
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        return user != null && UserConstant.ADMIN_ROLE.equals(user.getUserRole());
    }

    // 用户注销
    @Override
    public boolean userLogout(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (user == null || user.getId() == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录错误");
        }
        // 从Session中移除登录态
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return true;
    }
}
