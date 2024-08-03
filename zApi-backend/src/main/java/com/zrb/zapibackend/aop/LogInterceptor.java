package com.zrb.zapibackend.aop;

import cn.hutool.core.date.StopWatch;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

@Aspect
@Component
@Slf4j
public class LogInterceptor {
    @Around("execution(* com.zrb.zapibackend.controller.*.*(..))")
    // 拦截controller包下所有类的所有方法
    public Object doInterceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        // 计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 获取请求对象
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 生成唯一请求id
        String requestId = UUID.randomUUID().toString();
        String url = request.getRequestURI();
        // 获取请求参数
        Object[] args = joinPoint.getArgs();
        String reqParam = "[" + StringUtils.join(args, ",") + "]";

        // 输出请求日志
        log.info("-------------------------request start-------------------------");
        log.info("id:{}, path:{}, ip:{}, params:{} ", requestId, url, request.getRemoteHost(), reqParam);
        log.info("---------------------------------------------------------------");

        // 执行原方法
        Object result = joinPoint.proceed();

        // 结束计时
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();

        log.info("--------------------------request end--------------------------");
        log.info("id:{}, cost:{}ms ", requestId, totalTimeMillis);
        log.info("---------------------------------------------------------------");

        return result;
    }
}
