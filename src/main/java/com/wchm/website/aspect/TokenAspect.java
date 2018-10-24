package com.wchm.website.aspect;

import com.github.pagehelper.util.StringUtil;
import com.wchm.website.annotation.UnToken;
import com.wchm.website.entity.Admin;
import com.wchm.website.service.AdminService;
import com.wchm.website.service.RedisService;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class TokenAspect {
    @Autowired
    private AdminService adminService;

    @Autowired
    private RedisService redisService;

    @Pointcut("execution(public * com.wchm.website.controller.AdminController.*(..))")
    public void addAdvice() {
    }

    @Around("addAdvice()")
    public Object aroundInterceptor(ProceedingJoinPoint pjp) throws Exception {
        Object result = null;

        String classType = pjp.getTarget().getClass().getName();
        Class<?> clazz = Class.forName(classType);
        String clazzName = clazz.getName(); // 获取类名

        Object[] args = pjp.getArgs(); // 获取所有参数
        Signature signature = pjp.getSignature();
        String methodName = signature.getName(); //获取方法名称

        // 获取参数名称和值
        Map<String, Object> nameAndArgs = getFieldsName(this.getClass(), clazzName, methodName, args);

        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod(); // 获取目标方法

        Method realMethod = pjp.getTarget().getClass().getDeclaredMethod(methodName, targetMethod.getParameterTypes());
        Object tokenAnnotation = realMethod.getAnnotation(UnToken.class);
        // 加了注解的不用拦截
        if (tokenAnnotation == null) {
            Object argsToken = nameAndArgs.get("token");
            if (argsToken != null) {
                String token = argsToken.toString();

                if (StringUtils.isEmpty(token)) { // token 为空
                    return "login";
                }

                // 判断redis里面是否存在token
                String redisStr  =  redisService.get(token);
                if(StringUtil.isEmpty(redisStr)){
                    return "login";
                }
            } else {
                return "login";
            }
        }

        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }


    private Map<String, Object> getFieldsName(Class cls, String clazzName, String methodName, Object[] args) throws NotFoundException {
        Map<String, Object> map = new HashMap<>();

        ClassPool pool = ClassPool.getDefault();
        // ClassClassPath classPath = new ClassClassPath(this.getClass());
        ClassClassPath classPath = new ClassClassPath(cls);
        pool.insertClassPath(classPath);

        CtClass cc = pool.get(clazzName);
        CtMethod cm = cc.getDeclaredMethod(methodName);
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            // exception
        }
        // String[] paramNames = new String[cm.getParameterTypes().length];
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < cm.getParameterTypes().length; i++) {
            map.put(attr.variableName(i + pos), args[i]);// paramNames即参数名
        }

        return map;
    }

//
//    @AfterThrowing(throwing="ex"
//            , pointcut="execution(* com.wchm.website.controller.*.*(..))")
//    public Object exceptionInterceptor(Throwable ex) {
//        System.out.println(ex.toString());
//        return "login";
//    }


}
