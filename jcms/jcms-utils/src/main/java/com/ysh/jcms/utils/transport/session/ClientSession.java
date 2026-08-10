package com.ysh.jcms.utils.transport.session;

import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.wire.Connection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClientSession — client-side session with pending request tracking.
 *
 * <p>
 * Generates ReqID values and matches incoming responses to pending requests.
 */
public class ClientSession extends Session {

    private final AtomicInteger nextReqId = new AtomicInteger(1);
    private final ConcurrentHashMap<Integer, PendingRequest> pendingRequests = new ConcurrentHashMap<>();
    private volatile long defaultTimeoutMs = 5000;

    public ClientSession(Connection connection) {
        super("cli-" + connection.socket().getLocalPort(), connection);
    }

    public ClientSession defaultTimeoutMs(long ms) {
        this.defaultTimeoutMs = ms;
        return this;
    }

    /** Return the next ReqID (1..65535, wraps around). */
    public int nextReqId() {
        int id = nextReqId.getAndIncrement();
        if (id > 65535) {
            id = 1;
            nextReqId.set(2);
        }
        return id;
    }

    public PendingRequest addPendingRequest(int reqId, long timeoutMs) {
        PendingRequest pr = new PendingRequest(reqId, timeoutMs);
        pendingRequests.put(reqId, pr);
        return pr;
    }

    public PendingRequest addPendingRequest(int reqId) {
        return addPendingRequest(reqId, defaultTimeoutMs);
    }

    public PendingRequest removePendingRequest(int reqId) {
        return pendingRequests.remove(reqId);
    }

    /** Number of requests currently awaiting a response. */
    public int pendingCount() {
        return pendingRequests.size();
    }

    /**
     * Try to dispatch an incoming frame to a matching pending request. Sets the
     * result on the PendingRequest so that waitForPendingRequest can unblock.
     *
     * @return true if matched, false if no pending request found
     */
    public boolean tryDispatchResponse(Frame frame) {
        int reqId = frame.reqId();
        PendingRequest pr = pendingRequests.get(reqId);
        if (pr != null) {
            pr.setResult(frame);
            return true;
        }
        return false;
    }

    /**
     * Block until the pending request for the given reqId has a result, then remove
     * it and return the result.
     *
     * @return the result (set by tryDispatchResponse), or null on timeout
     */
    public Object waitForPendingRequest(int reqId, long timeoutMs) throws InterruptedException {
        PendingRequest pr = pendingRequests.get(reqId);
        if (pr == null)
            return null;
        Object result = pr.waitForResult();
        pendingRequests.remove(reqId);
        return result;
    }
}
