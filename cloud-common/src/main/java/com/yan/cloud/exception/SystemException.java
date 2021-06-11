package com.yan.cloud.exception;

public class SystemException extends BaseException {

    public SystemException(Integer msgCode, Object[] args, String errorMsg) {
        super("cloud-system", msgCode, args, errorMsg);
    }
}
