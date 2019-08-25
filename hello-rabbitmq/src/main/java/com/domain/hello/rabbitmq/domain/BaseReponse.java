package com.domain.hello.rabbitmq.domain;

/**
 * @description:
 * @author: domain
 * @create: 2019/8/24 17:53
 */

public class BaseReponse {

    public static final String SUCCESS="20000";

    private String resultCode;
    private String resultMsg;


    public BaseReponse() {
    }

    public BaseReponse(String resultMsg) {
        this.resultCode=SUCCESS;
        this.resultMsg = resultMsg;
    }

    public BaseReponse(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }



    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}
