package com.yan.cloud.exception;

import java.util.Arrays;

public class BaseException extends RuntimeException {

    private String modelCode;
    private Integer msgCode;
    private Object[] args;
    private String errorMsg;

    public BaseException(String modelCode, Integer msgCode,
                         Object[] args, String errorMsg) {
        this.modelCode = modelCode;
        this.msgCode = msgCode;
        this.args = args;
        this.errorMsg = errorMsg;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public Integer getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(Integer msgCode) {
        this.msgCode = msgCode;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "BaseException{" +
                "modelCode='" + modelCode + '\'' +
                ", msgCode=" + msgCode +
                ", args=" + Arrays.toString(args) +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
