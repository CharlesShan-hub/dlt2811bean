package com.ysh.dlt2811bean.transport.session;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.transport.io.CmsConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Client-side session.
 *
 * <p>Manages the pending request table and response dispatch.
 * Responses are matched to requests by ReqID and wake up the waiting thread.
 */
public class CmsClientSession extends CmsSession {

    private static final Logger log = LoggerFactory.getLogger(CmsClientSession.class);

    /** Default request timeout in milliseconds. */
    public static final long DEFAULT_TIMEOUT_MS = 5000;

    private final AtomicInteger nextReqId = new AtomicInteger(1);
    private final ConcurrentHashMap<Integer, PendingRequest> pendingRequests = new ConcurrentHashMap<>();

    private long defaultTimeoutMs = DEFAULT_TIMEOUT_MS;

    public CmsClientSession(CmsConnection connection) {
        super("cli-" + connection.getSocket().getPort(), connection);
    }

    // ==================== ReqID ====================

    /**
     * Returns the next ReqID (wraps at 65535).
     * A ReqID of 0 is reserved for non-request services (e.g., Report).
     */
    public int nextReqId() {
        int id = nextReqId.getAndIncrement();
        if (id > 65535) {
            id = 1;
            nextReqId.set(2);
        }
        return id;
    }

    // ==================== Pending Requests ====================

    /**
     * Registers a pending request.
     *
     * @param reqId   the request ID
     * @param timeoutMs timeout in milliseconds
     * @return the PendingRequest
     */
    public PendingRequest addPendingRequest(int reqId, long timeoutMs) {
        PendingRequest pr = new PendingRequest(reqId, timeoutMs);
        pendingRequests.put(reqId, pr);
        return pr;
    }

    /**
     * Registers a pending request with the default timeout.
     */
    public PendingRequest addPendingRequest(int reqId) {
        return addPendingRequest(reqId, defaultTimeoutMs);
    }

    /**
     * Finds and removes a pending request by ReqID.
     *
     * @param reqId the request ID
     * @return the PendingRequest, or null if not found
     */
    public PendingRequest removePendingRequest(int reqId) {
        return pendingRequests.remove(reqId);
    }

    /**
     * Looks up a pending request without removing it.
     */
    public PendingRequest getPendingRequest(int reqId) {
        return pendingRequests.get(reqId);
    }

    // ==================== Dispatch ====================

    /**
     * Dispatches a received APDU to its matching pending request (if any).
     *
     * <p>If the APDU matches a pending request, the result is set and the waiting thread
     * is woken up. If no pending request matches, the APDU is silently ignored.
     *
     * <p>Test service (SC=0x99, FL=0) has no ReqID and no ASDU — it is not matched
     * against pending requests. It is simply ignored here; the caller (CmsClient.send)
     * detects the Test service and handles it directly.
     *
     * @param apdu the received APDU
     */
    public void dispatchResponse(CmsApdu apdu) {
        // Test service (SC=0x99) has no ReqID and no ASDU — skip matching.
        if (apdu.getApch().getServiceCode() == ServiceName.TEST) {
            log.debug("Received Test response, ignoring (no ReqID to match)");
            return;
        }

        int reqId = apdu.getReqId();
        PendingRequest pending = pendingRequests.remove(reqId);
        if (pending != null) {
            // Extract associationId from Associate response
            CmsAsdu<?> asdu = apdu.getAsdu();
            if (asdu != null && asdu.getServiceName() == ServiceName.ASSOCIATE) {
                CmsAssociate assocResp = (CmsAssociate) asdu;
                if (assocResp.associationId() != null && assocResp.associationId().get() != null) {
                    setAssociationId(assocResp.associationId().get());
                }
            }
            pending.setResult(apdu);
        }
    }

    // ==================== Timeout ====================

    public long getDefaultTimeoutMs() {
        return defaultTimeoutMs;
    }

    public void setDefaultTimeoutMs(long defaultTimeoutMs) {
        this.defaultTimeoutMs = defaultTimeoutMs;
    }

    // ==================== Lifecycle ====================

    @Override
    public void onDisconnected() {
        super.onDisconnected();
        // Cancel all pending requests
        for (PendingRequest pr : pendingRequests.values()) {
            pr.setResult(null);  // null = disconnected
        }
        pendingRequests.clear();
    }
}
