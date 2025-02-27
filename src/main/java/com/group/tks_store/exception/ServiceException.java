package com.group.tks_store.exception;

import com.group.tks_store.exception.interfaze.ExceptionAwareness;
import com.group.tks_store.exception.interfaze.HttpCodeAwareness;
import org.springframework.http.HttpStatus;

public class ServiceException extends RuntimeException implements ExceptionAwareness {

    private static final long serialVersionUID = -2352432061610688307L;

    protected String errorCode;
    protected HttpCodeAwareness systemCode;

    @Override
    public String getCode() {
        return errorCode;
    }

    @Override
    public HttpCodeAwareness getSystemCode() {
        return systemCode;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return ExceptionAwareness.super.getHttpStatus();
    }

    public ServiceException(HttpCodeAwareness httpCodeAwareness) {
        super(httpCodeAwareness == null ? null : httpCodeAwareness.getCode());
        this.systemCode = httpCodeAwareness;
        if(systemCode != null) {
            this.errorCode = httpCodeAwareness.getCode();
        }
    }

    public ServiceException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public ServiceException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ServiceException(String errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
    }
}
