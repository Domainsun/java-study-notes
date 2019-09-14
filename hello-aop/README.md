目录

[TOC]

---

# 是个什么东西

面向对象，大家都听过。那面向切面是个啥？面向切面是对面向对象的延伸。那就先从面向对象开始说起。

## 纵向抽取

在面向对象中， 对重复的逻辑代码会抽取出来，在需要用到的地方继承即可。这就是`纵向抽取`。

## 横向抽象
但是，纵向抽取需求并不能满足所有的抽取场景，比如在不同的方法有着一些重复的代码逻辑，通过纵向抽取是没办法抽取使用的，代码如下:

![主业务和系统业务耦合](https://upload-images.jianshu.io/upload_images/5291509-3af7ee130970a4c6?imageMogr2/auto-orient/strip|imageView2/2/format/webp)

所以我们需要使用横向切面的方式来对重复的逻辑代码进行抽取复用，这就是面向切面编程:
>将分散在各个业务逻辑代码中相同的代码通过横向切割的方式抽取到一个独立的模块中

![image](https://upload-images.jianshu.io/upload_images/5291509-77a257a3db4893dc?imageMogr2/auto-orient/strip|imageView2/2/w/393/format/webp)

在日常的开发中，代码分为主业务和系统级业务，上图中的左侧的就是主业务逻辑，右侧就是系统级业务。系统级业务都应该用切面的方式抽取到独立的模块中，不和主业务代码耦合，真正实现写代码时只需关注主业务的实现。


# 有哪些概念

[相关概念](https://www.jianshu.com/p/21a5d7579c0e/)

# 写代码实践

通过定义一个权限检查切面，来对所有的接口进行权限校验。

## 编写两个接口
 
一个添加接口，一个删除接口，删除接口需要userType为admin才有权限删除。
 
```

@RestController
public class WebController {

    @GetMapping("add")
    public String add(@RequestParam("userType") String userType){
        System.out.println("add：添加用户...");
        return "ok";
    }

    @GetMapping(value = "del")
    public String del(@RequestParam("userType") String userType){
        System.out.println("add：删除用户...");
        return "ok";
    }

}
```

## 编写权限检查AOP

### 定义切点

```
    /**
     * 定义一个切点
     *
     * 通过匹配注解的方式切进来,所有被GetMapping 注解的方法都会通过这个切点进来
     *
     */
    @Pointcut(value = "@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void permissionCut(){}

```

### 实现切面逻辑
#### 目标方法前后增强方法
```
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
                //执行目标方法
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
```
#### 其他增强方法

```
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
```



# 完整项目代码

https://github.com/domain9065/java-study-notes/tree/master/hello-aop