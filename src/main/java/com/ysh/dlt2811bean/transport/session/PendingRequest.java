package com.ysh.dlt2811bean.transport.session;

/**
 * Holds information about a pending request awaiting a response.
 *
 * <p>Used by {@link CmsClientSession} to match responses to requests
 * via ReqID, and to support synchronous wait with timeout.
 */
public class PendingRequest {

    private final int reqId;
    private final long createTime;
    private final long expireTime;
    private volatile Object result;
    private volatile boolean done;

    /**
     * Creates a pending request.
     *
     * @param reqId    the request ID (1..65535)
     * @param timeoutMs timeout in milliseconds
     */
    public PendingRequest(int reqId, long timeoutMs) {
        this.reqId = reqId;
        this.createTime = System.currentTimeMillis();
        this.expireTime = createTime + timeoutMs;
    }

    public int getReqId() {
        return reqId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expireTime;
    }

    /**
     * Sets the result and marks as done.
     *
     * @param result the response result
     */
    public synchronized void setResult(Object result) {
        this.result = result;
        this.done = true;
        notifyAll();
    }

    public synchronized Object getResult() {
        return result;
    }

    public boolean isDone() {
        return done;
    }

    /**
     * Waits for the response with the configured timeout.
     *
     * @return the result, or null if timed out
     */
    public synchronized Object await(long timeoutMs) throws InterruptedException {
        long remaining = expireTime - System.currentTimeMillis();
        if (remaining <= 0) {
            return null;
        }
        while (!done && remaining > 0) {
            wait(remaining);
            remaining = expireTime - System.currentTimeMillis();
        }
        return result;
    }
}
