
[toc]

> # 前言

前面说到，要用微服务， 就要解决微服务的几个核心问题 具体可看[第一篇文章](https://juejin.im/post/5db7e09ff265da4d0f140855)。这篇文章是学习使用Spring Cloud Netflix 的 Eureka 组件。通过这个组件，我们可以创建一个服务的注册与发现中心服务来管理我们服务。这是解决之后的其他问题的基础。只有所有的服务都注册到注册中心中， 才有利于做后续的服务间调用和服务监控等。
> # 创建统一的依赖管理项目
---
> ## 为什么要创建依赖管理项目?
在创建服务注册中心的项目前， 我们先创建一个依赖管理项目。为什么要有依赖管理项目?
1. 由于微服务创建的项目很多， 项目中使用到的依赖会有大部分相同的,需要利用maven的可继承属性把相同依赖抽取出来。
2. 统一管理依赖包的版本号。更改方便，只需要在这一个地方找就行了。 避免不同版本号造成依赖冲突。
3. 人家spring都是这么做的... 好吧， 这是开玩笑的。不过spring 确认是这样做的。点开`spring-boot-starter-parent`这个依赖可以看到它的父pom就是一个依赖管理项目（`spring-boot-dependencies`）

> ## 创建项目
创建一个目录名为`hello-spring-cloud-dependencies`， 新建pom文件，内容如下:

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.3.RELEASE</version>
    </parent>

    <groupId>com.domain</groupId>
    <artifactId>hello-spring-cloud-dependencies</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>

    <name>hello-spring-cloud-dependencies</name>
    <url>http://www.baidu.com</url>
    <inceptionYear>2018-Now</inceptionYear>

    <properties>
        <!-- Environment Settings -->
        <java.version>1.8</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>

        <!-- Spring Settings -->
        <spring-cloud.version>Finchley.RELEASE</spring-cloud.version>
        <spring-boot-admin.version>2.0.1</spring-boot-admin.version>
        <zipkin.version>2.10.1</zipkin.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <!-- Spring Cloud Begin  微服务框架-->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!-- Spring Cloud End -->

            <!-- Spring Boot Admin Begin    用来管理和监控spring boot 应用程序-->
            <dependency>
                <groupId>de.codecentric</groupId>
                <artifactId>spring-boot-admin-starter-server</artifactId>
                <version>${spring-boot-admin.version}</version>
            </dependency>
            <dependency>
                <groupId>de.codecentric</groupId>
                <artifactId>spring-boot-admin-starter-client</artifactId>
                <version>${spring-boot-admin.version}</version>
            </dependency>
            <!-- Spring Boot Admin End -->

            <!-- ZipKin Begin  链路追踪系统-->
            <dependency>
                <groupId>io.zipkin.java</groupId>
                <artifactId>zipkin</artifactId>
                <version>${zipkin.version}</version>
            </dependency>
            <dependency>
                <groupId>io.zipkin.java</groupId>
                <artifactId>zipkin-server</artifactId>
                <version>${zipkin.version}</version>
            </dependency>
            <dependency>
                <groupId>io.zipkin.java</groupId>
                <artifactId>zipkin-autoconfigure-ui</artifactId>
                <version>${zipkin.version}</version>
            </dependency>
            <!-- ZipKin End -->
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>

        <!-- 资源文件配置 -->
        <resources>
            <resource>
                <directory>src/main/java</directory>
                <excludes>
                    <exclude>**/*.java</exclude>
                </excludes>
            </resource>
            <resource>
                <directory>src/main/resources</directory>
            </resource>
        </resources>

    </build>
</project>
```

大致说下pom文件内容意思， parent 继承`spring-boot-starter-parent`表示这是一个`spring boot`项目,然后声明了，项目的组织，项目名版本等。然后在`dependencyManagement`节点声明了 一些依赖， 这些依赖由于声明在`dependencyManagement`节点中， 所以并真实依赖进本项目或者是子项目，只是起了一个声明作用。 如果需要真实依赖使用， 需要在使用地方再次声明一次，这次声明不带版本。可以看出， 我们这个依赖管理项目， 只是把依赖抽取到这个地方统一管理而已。如果实在看不懂pom文件内容的，建议去补maven知识。


> # 创建服务注册中心项目
依赖管理项目创建好了， 现在要做的创建一个服务注册中心项目继承它。pom内容如下:
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.domain</groupId>
        <artifactId>hello-spring-cloud-dependencies</artifactId>
        <version>1.0.0-SNAPSHOT</version>
        <relativePath>../hello-spring-cloud-dependencies/pom.xml</relativePath>
    </parent>

    <artifactId>hello-spring-cloud-eureka</artifactId>
    <packaging>jar</packaging>

    <name>hello-spring-cloud-eureka</name>
    <url>http://www.baidu.com</url>
    <inceptionYear>2018-Now</inceptionYear>

    <dependencies>
        <!-- Spring Boot Begin -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!-- Spring Boot End -->

     
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
 
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <mainClass>com.domain.hello.spring.cloud.eureka.EurekaApplication</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

**内容说明:**这里就两个依赖， 一个是spring boot测试包依赖， 一个是`eureka`依赖， 这个就是Spring cloud 提供的服务注册与发现组件， 当我们微服务中的各个服务启动后， 会自动注册到这个注册中心， 方便我们对各个服务管理、使用。

> ## eureka配置
配置文件内容如下:

```
spring:
  application:
    name: hello-spring-cloud-eureka
server:
  port: 8881
eureka:
  instance:
    hostname: localhost
  client:
    register-with-eureka: false
    fetch-registry: false
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/

```
**内容说明:** 前面是配置我们服务的名称，端口。eureka 这个节点下面是eureka的相关配置，hostname是我们服务器地址， client的两个属性是配置是否客户端， 这里写false，表示这是eureka的服务端。其他服务要作为客户端来注册到我们这个服务端。defaultZone 是eureka的地址。客户端就是通过这个地址来注册到我们
eureka的服务端。

> ## 服务查看页面
当服务注册到eureka后， 我们可以通过一个web 页面来查看。启动服务后，访问服务地址。 
![服务查看页面](https://s2.ax1x.com/2019/10/31/KTCM2n.md.png)

页面中红色框选的地方会显示当前所有注册到eureka的服务。不过当前还没有服务注册上去。等下一篇文章节说服务提供者的， 我们把服务注册上去，就能看见了。
> # 完整代码
完整代码在这里: https://github.com/domain9065/java-study-notes/tree/master/hello-spring-cloud/hello-spring-cloud-eureka
> # 所有文章目录及说明
有关文章的说明和目录都在这里了:https://juejin.im/post/5dbadf9f6fb9a0204e658da8