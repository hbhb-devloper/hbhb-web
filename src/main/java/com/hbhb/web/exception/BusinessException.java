package com.hbhb.web.exception;

import com.hbhb.core.enums.ResultCode;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = -599033311976732297L;

    private final String code;

    public BusinessException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }

    public String getCode() {
        return code;
    }
}
