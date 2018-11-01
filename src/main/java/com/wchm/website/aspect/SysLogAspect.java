
package com.wchm.website.aspect;

import com.wchm.website.annotation.MyLog;
import com.wchm.website.entity.Admin;
import com.wchm.website.entity.Operation;
import com.wchm.website.mapper.AdminMapper;
import com.wchm.website.service.OperationService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;


@Aspect
@Component     //每个切面最终还是要扫面到bin容器里面去，成为bin容器的组件
public class SysLogAspect {

    @Autowired
    private OperationService operationService;

    @Autowired
    AdminMapper adminMapper;

    //定义切点 @Pointcut
    //在注解的位置切入代码
    @Pointcut("execution(public * com.wchm.website.controller.AdminController.*(..))")
    public void logPoinCut() {
    }

    /**
     * 正常返回通知
     *
     * @param joinPoint
     */
    //切面类配置正常返回通知
    @AfterReturning("logPoinCut()")
    public void saveSysLog(JoinPoint joinPoint) {
        saveLog(joinPoint, 1);
    }

    /**
     * 异常返回通知
     *
     * @param joinPoint
     */
    //切面类配置异常返回通知
    @AfterThrowing("logPoinCut()")
    public void saveSysLogThrowing(JoinPoint joinPoint) {
        saveLog(joinPoint, 0);
    }


    private void saveLog(JoinPoint joinPoint, int state) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取操作
        Method realMethod = null;
        try {
            realMethod = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(), method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        MyLog myLog = realMethod.getAnnotation(MyLog.class);
        // 判断Controller的方法是否有@MyLog有则将操作记录插入数据库
        if (myLog != null) {
            Operation sysLog = new Operation();
            String value = myLog.value();
            // 保存获取的操作类型
            sysLog.setOperation_type(value);
            // 获取登入用户名
            Subject currentUser = SecurityUtils.getSubject();
            Admin admin = (Admin) currentUser.getPrincipals().getPrimaryPrincipal();
            // 操作时间
            sysLog.setCreate_time(new Date());
            // 获取用户名
            sysLog.setAdmin_name(admin.getUsername());
            // 保存状态（ 1成功 0失败）
            sysLog.setState(state);
            // 保存到数据库
            operationService.operationSave(sysLog);
        }
    }

}


