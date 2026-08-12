package com.ysh.jcms.utils.transport.session;

import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.wire.Connection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * ClientSession — client-side session with pending request tracking.
 *
 * <p>
 * Generates ReqID values and matches incoming responses to pending requests.
 */
public class ClientSession extends Session {

    private final AtomicInteger nextReqId = new AtomicInteger(1);
    private final ConcurrentHashMap<Integer, PendingRequest> pendingRequests = new ConcurrentHashMap<>();
    @Setter
    @Accessors(fluent = true, chain = true)
    private volatile long defaultTimeoutMs = 5000;

    public ClientSession(Connection connection) {
        super("cli-" + connection.socket().getLocalPort(), connection);
    }

    /** Return the next ReqID (1..65535, wraps around), per DL/T 2811 6.2.1 a). */
    public int nextReqId() {
        int id = nextReqId.getAndIncrement();
        if (id > 65535) {
            id = 1;
            nextReqId.set(2);
        }
        return id;
    }

    public PendingRequest addPendingRequest(int reqId, long timeoutMs) {
        // DL/T 2811 6.9.1: arm a timeout timer when the request is sent
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
     * Clear all pending requests on teardown: wake waiters (they get null) so the
     * map cannot grow unbounded after disconnect.
     */
    @Override
    protected void clearConnection() {
        super.clearConnection();
        for (PendingRequest pr : pendingRequests.values()) {
            pr.setResult(null);
        }
        pendingRequests.clear();
    }

    /**
     * Try to dispatch an incoming frame to a matching pending request. Sets the
     * result on the PendingRequest so that waitForPendingRequest can unblock. Per
     * DL/T 2811 6.2.1 b): the responder must reply with the request's ReqID.
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
     * it and return the result. Times out per DL/T 2811 6.9.1.
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
