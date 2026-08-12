package com.ysh.jcms.utils.transport.service;

/**
 * Result of a dispatch operation.
 */
public enum DispatchResult {
    /** Handler processed the request successfully. */
    HANDLED,
    /** No handler registered for this service code. */
    NOT_REGISTERED,
    /** Handler threw an exception. */
    ERROR_OCCURRED
}
