package com.ysh.jcms;

public class CmsException extends RuntimeException {

    public CmsException(String message) {
        super(message);
    }

    public CmsException(String message, Throwable cause) {
        super(message, cause);
    }
}
