package com.chalo.exception;

import org.springframework.stereotype.Component;

@Component
public class MyException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private String errCode;
    private String errMsg;
    private String errDetailMsg;

    public MyException() { }

    public MyException(String errCode, String errMsg, String errDetailMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.errDetailMsg = errDetailMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrDetailMsg() {
        return errDetailMsg;
    }

    public void setErrDetailMsg(String errDetailMsg) {
        this.errDetailMsg = errDetailMsg;
    }
}
