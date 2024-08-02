package com.moirrra.community.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-12
 * @Description:
 * @Version: 1.0
 */
@Component
@Aspect
@Slf4j
public class ServiceLogAspect {

    @Pointcut("execution(* com.moirrra.community.service.*.*(..))")
    public void pointcut() {

    }

    // @Before("pointcut()")
    // public void before(JoinPoint joinPoint) {
    //     // 用户[1.2.3.4],在[xxx],访问了[com.moirrra.community.service.xxx()].
    //     ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    //     if (attributes == null) {
    //         return;
    //     }
    //     HttpServletRequest request = attributes.getRequest();
    //     String ipAddr = request.getRemoteHost();
    //     String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    //     String target = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
    //     log.info(String.format("用户[%s],在[%s],访问了[%s].", ipAddr, now, target));
    // }

    // @After("pointcut()")
    // public void after() {
    //     System.out.println("after");
    // }
    //
    // @AfterReturning("pointcut()")
    // public void afterReturning() {
    //     System.out.println("afterReturning");
    // }
    //
    // @AfterThrowing("pointcut()")
    // public void afterThrowing() {
    //     System.out.println("afterThrowing");
    // }
    //
    // @Around("pointcut()")
    // public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
    //     System.out.println("around before");
    //     Object obj = joinPoint.proceed();// 调用目标组件的方法
    //     System.out.println("around after");
    //     return obj;
    // }

}
