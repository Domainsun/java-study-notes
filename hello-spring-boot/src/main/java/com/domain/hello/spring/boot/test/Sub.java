package com.domain.hello.spring.boot.test;

public class Sub extends Base {
    @Override
    public String doGet() {
        return "get";
    }

    @Override
    public String doPost() {
        return "domain";
    }

    public static void main(String[] args) {
        Sub sub = new Sub();
        System.out.println(sub.getResult());
        System.out.println(sub.getThis());

    }
}
