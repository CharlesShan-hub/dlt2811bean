package com.ysh.jcms.utils.transport.session;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Holds information about a pending request awaiting a response. Implements the
 * request timeout timer of DL/T 2811 6.9.1.
 */
@Getter
@Accessors(fluent = true)
public class PendingRequest {

    private final int reqId;
    private final long createTime;
    private final long expireTime;
    private volatile Object result;
    private volatile boolean done;

    public PendingRequest(int reqId, long timeoutMs) {
        this.reqId = reqId;
        this.createTime = System.currentTimeMillis();
        this.expireTime = createTime + timeoutMs;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expireTime;
    }

    public synchronized void setResult(Object result) {
        this.result = result;
        this.done = true;
        notifyAll();
    }

    public synchronized Object waitForResult() throws InterruptedException {
        long remaining = expireTime - System.currentTimeMillis();
        while (!done && remaining > 0) {
            wait(remaining);
            remaining = expireTime - System.currentTimeMillis();
        }
        return result;
    }
}
