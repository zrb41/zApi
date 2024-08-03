package com.zrb.zapibackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zrb.zapicommon.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;


public interface UserService extends IService<User> {

    // 用户注册
    long userRegister(String userAccount, String userPassword, String checkPassword);

    // 用户登录
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    // 获取当前登录用户
    User getLoginUser(HttpServletRequest request);

    // 是否为管理员
    boolean isAdmin(HttpServletRequest request);

    // 用户注销
    boolean userLogout(HttpServletRequest request);
}
