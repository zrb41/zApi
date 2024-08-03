package com.zrb.zapibackend.aop;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.zrb.zapibackend.annotation.AuthCheck;
import com.zrb.zapibackend.common.ErrorCode;
import com.zrb.zapibackend.exception.BusinessException;
import com.zrb.zapibackend.service.UserService;
import com.zrb.zapicommon.model.entity.User;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    @Around("@annotation(authCheck)")
    // 对添加了AuthCheck注解的方法进行增强
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable{
        // 获取anyRole字段里的内容
        List<String> anyRole = Arrays.stream(authCheck.anyRole()).filter(StringUtils::isNotBlank).toList();
        // 获取mustRole字段里的内容
        String mustRole = authCheck.mustRole();
        // 拿到当前请求
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 获取当前登录用户
        User user = userService.getLoginUser(request);

        // 拥有任意权限即可通过
        if(CollectionUtils.isNotEmpty(anyRole)){
            String userRole = user.getUserRole();
            if(!anyRole.contains(userRole)){
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }

        // 验证当前登录用户的角色是否于指定权限相匹配
        if (StringUtils.isNotBlank(mustRole)) {
            String userRole = user.getUserRole();
            if (!mustRole.equals(userRole)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }
        // 通过权限校验，放行
        return joinPoint.proceed();
    }

}
