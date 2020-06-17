package com.yan.cloud.exception;

public class TokenAuthenticationException extends Exception {
    /**
     * 异常码
     */
    protected int code;

    public TokenAuthenticationException() {}
    public TokenAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }
    public TokenAuthenticationException(int code, String msg) {
        super(msg);
        this.code = code;
    }


    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
}
