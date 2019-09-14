package com.domain.hello.aop.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @description: 权限切面
 * @author: domain
 * @create: 2019/9/14 14:35
 */

@Aspect
@Component
public class PermissionAop {


    /**
     * 定义一个切点
     *
     * 通过匹配注解的方式切进来,所有被GetMapping 和 PostMapping 注解的方法都会通过这个切点进来
     *
     */
    @Pointcut(value = "@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void permissionCut(){}


    @Around(value = "permissionCut()")
    public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("@Around：开始校验权限...");
        Signature sig = joinPoint.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Method currentMethod = msig.getMethod();


        //获取GetMapping注解中的路径
        String methodMapping = null;
        GetMapping methodGetMapping = currentMethod.getAnnotation(GetMapping.class);
        if(methodGetMapping!=null&&methodGetMapping.value()!=null&&methodGetMapping.value().length>0) {
            methodMapping = methodGetMapping.value()[0];
        }

        //访问目标方法的参数：
        String userType = "";
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0 && args[0].getClass() == String.class) {
            userType = (String) args[0];
        }

        //删除接口需要验证管理员权限
        if ("del".equals(methodMapping)){
            if (userType.equals("admin")) {
                joinPoint.proceed();
                System.out.println("@Around：满足权限...");
                return "ok";
            }else {
                System.out.println("@Around：不满足权限...");
                return "无权限访问";
            }
        }
            return joinPoint.proceed();

    }



    @Before("permissionCut()")
    public void permissionCheck(JoinPoint point) {
        System.out.println("@Before：开始权限检查...");
        System.out.println("@Before：目标方法为：" +
                point.getSignature().getDeclaringTypeName() +
                "." + point.getSignature().getName());
        System.out.println("@Before：参数为：" + Arrays.toString(point.getArgs()));
        System.out.println("@Before：被织入的目标对象为：" + point.getTarget());
    }

    @AfterReturning(pointcut="permissionCut()",
            returning="returnValue")
    public void log(JoinPoint point, Object returnValue) {
        System.out.println("@AfterReturning：开始记录返回值...");
        System.out.println("@AfterReturning：目标方法为：" +
                point.getSignature().getDeclaringTypeName() +
                "." + point.getSignature().getName());
        System.out.println("@AfterReturning：参数为：" +
                Arrays.toString(point.getArgs()));
        System.out.println("@AfterReturning：返回值为：" + returnValue);
        System.out.println("@AfterReturning：被织入的目标对象为：" + point.getTarget());

    }

    @After("permissionCut()")
    public void releaseResource(JoinPoint point) {
        System.out.println("@Before：结束权限检查...");
    }




}
