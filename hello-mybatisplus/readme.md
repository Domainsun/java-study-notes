[toc]
# 介绍

`MyBatis` 是一个java的半自动化`ORM`框架，而MyBatis-Plus是对它的二次封装，简化MyBatais的使用，简单的说， 可以尽可能少的手写增删改查的`sql`语句 。



官方文档地址：https://mp.baomidou.com/guide

文档是中文的， 喜欢自己看文档的同学可以直接照官方文档来操作学习。 我这篇文章主要是记录自己的学习过程，最后会提供自己的代码及测试的数据库环境部署。不想自己弄这些步骤的同学可以参照我这篇文章部署好环境来学习。 

# 部署数据库

- 启动数据库容器
使用docker容器在虚拟机中部署数据库很简单，只需要一个docker-compose.yml文件， 下面是docker-compose.yml完整文件内容:
```
version: '3.1'
services:
  mysql:
    restart: always
    image: mysql:5.7.22
    container_name: mysql
    ports:
      - 3306:3306
    environment:
      TZ: Asia/Shanghai
      MYSQL_ROOT_PASSWORD: 123456
    command:
      --character-set-server=utf8mb4
      --collation-server=utf8mb4_general_ci
      --explicit_defaults_for_timestamp=true
      --lower_case_table_names=1
      --max_allowed_packet=128M
      --sql-mode="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION,NO_ZERO_DATE,NO_ZERO_IN_DATE,ERROR_FOR_DIVISION_BY_ZERO"
    volumes:
      - ./data:/var/lib/mysql
```

不会使用docker容器可以参考 [这篇文章](https://juejin.im/post/5d2ac1946fb9a07ebb056229) 来学习


# Spring Boot 整合 MyBatis-Plus

项目是基于Spring Boot的， 创建项目后，添加各项依赖，完整pom.xml文件如下:

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.6.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.domain</groupId>
    <artifactId>hello-mybatisplus</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>hello-mybatisplus</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>


        <!--mysql start-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.10</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!--mysql end-->

        <!--lombok start-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <!--lombok end -->


        <!--mybatisplus start-->


        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.1.2</version>
        </dependency>


        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-generator</artifactId>
            <version>3.1.2</version>
        </dependency>

        <dependency>
            <groupId>org.apache.velocity</groupId>
            <artifactId>velocity-engine-core</artifactId>
            <version>2.1</version>
        </dependency>


        <!--mybatisplus end-->

    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>


        </plugins>
    </build>

</project>

```

主要添加了数据库连接、和MyBatis-Plus的依赖。

# 连接数据库

在application.yml中添加数据库连接配置:

```
spring:
  datasource:
    druid:
      url: jdbc:mysql://192.168.65.130:3306/myshop?useUnicode=true&characterEncoding=utf-8&useSSL=false
      username: root
      password: 123456
      initial-size: 1
      min-idle: 1
      max-active: 20
      test-on-borrow: true
      # MySQL 8.x: com.mysql.cj.jdbc.Driver
      driver-class-name: com.mysql.jdbc.Driver
      test-while-idle: true
      
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

主要配置了数据库的地址，用户名，密码， 和打开mybatis-plus的sql日志， 这样待会测试的时候日志就会打印出当前数据库操作的sql语句，便于学习和排错。


# 代码生成器

MyBatis-Plus 提供了代码生成器的功能，能自动帮我们生成`mapper.xml`、 `Mapper.java` `POJO` 等文件。运行一下代码来生成这些文件:

```
package com.domain.hello.mybatisplus;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CodeGenerator {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("domain");
        gc.setOpen(false);
//        gc.setSwagger2(true); //实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://192.168.65.130:3306/myshop?useUnicode=true&useSSL=false&characterEncoding=utf8");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
//        pc.setModuleName(scanner("模块名"));
        pc.setParent("com.domain.hello.mybatisplus");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
//        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
         String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" /*+ pc.getModuleName()*/
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });



        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录");
                return false;
            }
        });
        */
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategy.setSuperEntityClass("com.domain.hello.mybatisplus.common.BaseEntity");
        strategy.setEntityLombokModel(true);
//        strategy.setRestControllerStyle(true);
        // 公共父类
//        strategy.setSuperControllerClass("com.domain.hello.mybatisplus.common.BaseController");
        // 写于父类中的公共字段
        strategy.setSuperEntityColumns("id");
//        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setInclude(new String[] { "tb_user", "tb_item_cat","tb_content"});
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);


//        strategy.setRestControllerStyle(false);
//        strategy.setSuperServiceClass(null);


//        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
//        mpg.setTemplateEngine(new VelocityTemplateEngine());
        mpg.execute();
    }

}

```

# CRUD 操作

在单元测试来执行`MyBatis-Plus`提供的`CRUD`操作

## 增
```
 /**
     * 测试插入数据
     */
    @Test
    public void testInsert() {
        // 构造一条测试数据
        for (int i = 0; i < 3; i++) {

            TbUser tbUser = new TbUser();
            tbUser.setUsername("domain" + RandomUtils.nextDouble());
            tbUser.setPassword("123456" + RandomUtils.nextDouble());
            tbUser.setPhone("130" + RandomUtils.nextInt());
            tbUser.setEmail("topsale@vip.qq.com" + RandomUtils.nextDouble());
            tbUser.setCreated(new Date());
            tbUser.setUpdated(new Date());

            // 插入数据
            tbUserMapper.insert(tbUser);
        }
    }
```

## 删

```
 @Test
    public void testDelete() {

        UpdateWrapper updateWrapper = new UpdateWrapper<TbUser>();

        HashMap<String, Object> params = new HashMap<>();
        params.put("id", 7);
        params.put("username", "zhangsan");

        updateWrapper.allEq(params);

        tbUserMapper.delete(updateWrapper);

//        tbUserMapper.deleteById("46");
//        ArrayList<Integer> ids = new ArrayList<Integer>();
//        ids.add(47);
//        tbUserMapper.deleteBatchIds(ids);

    }
```

## 改

```
 @Test
    public void testUpdate() {

        UpdateWrapper<TbUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("username", "domain111");

        TbUser tbUser = new TbUser();
        tbUser.setUsername("domain");

        tbUserMapper.update(tbUser, updateWrapper);

    }
```

## 查

```
    @Test
    public void testSelect() {
        UpdateWrapper<TbUser> wrapper = new UpdateWrapper<>();
        wrapper.eq("username", "domain"); //等于

        List<TbUser> tbUsers = tbUserMapper.selectList(wrapper);
        for (TbUser tbUser : tbUsers) {
            System.out.println("\n" + tbUser.getUsername());
        }
    }
```

## 分页

MyBatis-Plus 也支持分页， 只需要简单配置即可以使用

### Config.java配置
```
package com.domain.hello.mybatisplus.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration
@MapperScan("com.domain.hello.mybatisplus.mapper.*.mapper*")
public class MybatisPlusConfig {
    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}

```

## 添加mapper 方法

mapper.java
```
@Repository
public interface TbUserMapper extends BaseMapper<TbUser> {

     List<TbUser> getAll();
     /**
      * <p>
      * 查询 : 根据state状态查询用户列表，分页显示
      * 注意!!: 如果入参是有多个,需要加注解指定参数名才能在xml中取值
      * </p>
      *
      * @param page 分页对象,xml中可以从里面进行取值,传递参数 Page 即自动分页,必须放在第一位(你可以继承Page实现自己的分页对象)
      * @param state 状态
      * @return 分页对象
      */
     IPage<TbUser> selectPageVo(Page page, @Param("city") String city);

}
```

mapper.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.domain.hello.mybatisplus.mapper.TbUserMapper">

    <select id="selectPageVo" resultType="com.domain.hello.mybatisplus.entity.TbUser">
    SELECT id,username FROM tb_user WHERE city=#{city}
    </select>

</mapper>

```

## 测试分页

```
    @Test
    public void testPage() {

        // 不进行 count sql 优化，解决 MP 无法自动优化 SQL 问题，这时候你需要自己查询 count 部分
        // page.setOptimizeCountSql(false);
        // 当 total 为小于 0 或者设置 setSearchCount(false) 分页插件不会进行 count 查询
        // 要点!! 分页返回的对象与传入的对象是同一个

        //页码下标
        Integer pageIndex = 1;
        //每页数量
        Integer pageSize = 2;

        IPage<TbUser> iPage = tbUserMapper.selectPageVo(new Page(pageIndex * pageSize, pageSize), "gz");


        List<TbUser> tbUsers = iPage.getRecords();

        for (TbUser tbUser : tbUsers) {
            System.out.println("\n" + tbUser.getUsername());
        }
    }
```


# 条件构造器

MyBatis-Plus 提供了条件构造器来构建sql语句的条件。

```
 @Test
    public void testSelect() {

        UpdateWrapper<TbUser> wrapper = new UpdateWrapper<>();
//        wrapper.eq("password","195d91be1e3ba6f1c857d46f24c5a454"); //等于
//        wrapper.ne("username","domain");//不等于
//        wrapper.gt("age",18); //大于
//        wrapper.ge("age",22); //大于等于
//        wrapper.lt("age", 22); //小于
//        wrapper.le("age", 22); //小于等于
//        wrapper.between("age","22","25"); //between value1 和 value2之间
//        wrapper.notBetween("age","22","25");//not between value1 和 value2之间, null 不会查出来
//        wrapper.like("username","domain"); %value%
//        wrapper.likeLeft("username","1");  %value
//        wrapper.likeRight("username","zhangsan"); value%
//        wrapper.isNull("age"); //age == null 的数据
//        wrapper.in("age","31","33"); age 是 31 33 的
//        wrapper.notIn("age","22","23");age 不是 22 22的， age为null的 不会查出来
//        wrapper.inSql("id", "select id from tb_user where id > 3"); // id 为 后面语句中查出来的id集合的 记录 相当于 where id in (4,5,6);       SELECT username,password,phone,email,created,updated FROM tb_user WHERE id IN (select id from tb_user where id > 3)
//        wrapper.notInSql("id","select id from tb_user where id < 22");//id 不小于 22 的记录
//        wrapper.notInSql("id","select id from tb_user where id < 22");//id 不小于 22 的记录,null 也查出来， 因为null 不在小于22的范围内
//        wrapper.groupBy("age"); //通过 age 分组，age相同为一组， 一组只有一条记录就是组的第一条记录
//        wrapper.orderByAsc("id","age");//以id，age 升序排序，先对id排序， id相同， 对age排序
//        wrapper.orderBy(true,true,"id","age"); //和上面效果相同
//        wrapper.orderByDesc("id","age");//以id，age 降序排序，先对id排序， id相同， 对age排序


        //having
        //    需要注意的是，
        //
        //    having 子句中的每一个元素也必须出现在select列表中。有些数据库例外，如oracle.
        //            having子句和where子句都可以用来设定限制条件以使查询结果满足一定的条件限制。
        //    having子句限制的是组，而不是行。where子句中不能使用聚集函数，而having子句中可以。
        //    当加上其他sql语句时，执行顺序如下：
        //    S-F-W-G-H-O 组合
        //
        //    select –>where –> group by–> having–>order by
        //
        //    顺序是不能改变的

//
//        SELECT
//                username,
//                city,
//        SUM(price)
//        FROM
//                tb_user
//        GROUP BY
//        city
//                HAVING
//        SUM(price) > 2000

//        wrapper.groupBy("city").having("sum(price)>2000");

//        wrapper.having("sum(age)>22");


//        wrapper.eq("username","domain").or().eq("age","22");  username = domain 或者 age = 22

        //OR 嵌套
//        wrapper.eq("username", "domain")
//                .or(i -> i.eq("age", "22").eq("phone", "13333333334"));// username = domain 或者 (age = 22 and phone = 13333333334)

        //and 嵌套
//        wrapper.eq("age","22")
//                .and(i -> i.eq("phone","13333333333").or().eq("phone","13333333334"));// age ==22 and (phone =13333333333 or phone = 13333333334)

        //nested ,正常嵌套 username=domain 或者 age<22
//        wrapper.nested(i -> i.eq("username","domain").or().lt("age","22"));


        wrapper.set("username", "domain").eq("username", "niuniu");

        List<TbUser> tbUsers = tbUserMapper.selectList(wrapper);


        /**
         * QueryWrapper， 可以指定要查询哪些字段
         */
//        QueryWrapper<TbUser> queryWrapper = new QueryWrapper<>();
//        queryWrapper.select("username","password","phone");
//        queryWrapper.eq("username","domain");
//        List<TbUser> tbUsers = tbUserMapper.selectList(queryWrapper);

        /**
         * 自定义 sql
         */
//        List<TbUser> tbUsers = tbUserMapper.getAll();


        for (TbUser tbUser : tbUsers) {
            System.out.println("\n" + tbUser.getUsername());
        }

    }
```


# 自定义sql

如果MyBatis-Plus 提供的CRUD方法没办法满足需求， 这时候就要自己手写sql来满足需求了。

比如查询 用户所在城市的的总金额大于2000 的记录。

## mapper.java 定义方法

```
@Repository
public interface TbUserMapper extends BaseMapper<TbUser> {

     List<TbUser> getAll();
}
```
## mapper.xml 定义sql

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.domain.hello.mybatisplus.mapper.TbUserMapper">

    <select id="getAll" resultType="com.domain.hello.mybatisplus.entity.TbUser">
        SELECT
            username,
            city,
            SUM(price) as amount
        FROM
            tb_user
        GROUP BY
            city
        HAVING
            SUM(price) > 2000

    </select>
</mapper>
```

## 测试自定义sql
```
@Test
    public void testSelect() {

        /**
         * 自定义 sql
         */
        List<TbUser> tbUsers = tbUserMapper.getAll();


        for (TbUser tbUser : tbUsers) {
            System.out.println("\n" + tbUser.getUsername());
        }
        
    }
```


# 完整代码

完整代码地址:`www.baidu.com`