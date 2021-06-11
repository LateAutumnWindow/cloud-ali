package com.yan.cloud.exception;

import com.yan.cloud.constant.MsgCode;

public class OrderExeption extends BaseException {

    public OrderExeption(Integer msgCode, Object[] args, String errorMsg) {
        super("cloud-order", msgCode, args, errorMsg);
    }
    public OrderExeption(Integer msgCode, String errorMsg) {
        super("cloud-order", msgCode, null, errorMsg);
    }
}
