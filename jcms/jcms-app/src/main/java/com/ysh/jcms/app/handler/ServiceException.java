package com.ysh.jcms.app.handler;

/**
 * Unchecked exception carrying a service error code for automatic error
 * response generation in {@link BaseServerHandler#handleRequest}.
 *
 * <p>
 * Throw from {@link BaseServerHandler#onDecodeSuccess} to abort processing and
 * return a structured error frame without manual null-checking.
 */
public class ServiceException extends RuntimeException {

    private final int reqId;
    private final int serviceError;

    public ServiceException(int reqId, int serviceError) {
        super("ServiceException reqId=" + reqId + " error=" + serviceError);
        this.reqId = reqId;
        this.serviceError = serviceError;
    }

    /** reqId to include in the error response frame. */
    public int reqId() {
        return reqId;
    }

    /**
     * Service error code (e.g.
     * {@link com.ysh.jcms.data.common.CmsServiceError#INSTANCE_NOT_AVAILABLE}).
     */
    public int serviceError() {
        return serviceError;
    }
}
