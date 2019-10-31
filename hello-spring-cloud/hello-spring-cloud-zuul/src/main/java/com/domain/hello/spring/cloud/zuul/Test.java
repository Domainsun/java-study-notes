package com.domain.hello.spring.cloud.zuul;

/**
 * @description:
 * @author: domain
 * @create: 2019/8/10 22:21
 */

public class Test {

    public static Integer five = 5;

    public static void main(String[] args) {
        compare(5);
        compare(new Integer(5));
    }

    private static void compare(Integer a){
        System.out.println(five == a);
        System.out.println(five.equals(a));
    }
}
