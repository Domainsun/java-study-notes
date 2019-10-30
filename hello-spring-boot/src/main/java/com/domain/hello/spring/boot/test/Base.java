package com.domain.hello.spring.boot.test;

abstract class Base {

    public abstract String doGet();

    public abstract String doPost();

    public String getResult(){
        return doGet();
    }

    public Base getThis(){
        return this;
    }

}
