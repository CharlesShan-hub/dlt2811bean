package com.ysh.dlt2811bean.transport.session;

import com.ysh.dlt2811bean.transport.io.CmsConnection;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Server-side session.
 *
 * <p>One instance exists per connected client. Manages the session lifecycle,
 * association ID, and dispatches APDUs to registered service handlers.
 */
public class CmsServerSession extends CmsSession {

    //private static final Logger log = LoggerFactory.getLogger(CmsServerSession.class);

    private final AtomicInteger nextReqId = new AtomicInteger(1);
    private final ConcurrentHashMap<Integer, Object> activeServices = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<SessionListener> listeners = new CopyOnWriteArrayList<>();

    private volatile String negotiatedApduSize;
    private volatile String negotiatedAsduSize;
    private volatile String negotiatedVersion;

    public CmsServerSession(CmsConnection connection) {
        super("srv-" + connection.getSocket().getPort(), connection);
    }

    // ==================== Identity ====================

    public InetSocketAddress getClientAddress() {
        return (InetSocketAddress) getConnection().getSocket().getRemoteSocketAddress();
    }

    // ==================== ReqID ====================

    /** Returns the next ReqID (wraps at 65535). */
    public int nextReqId() {
        int id = nextReqId.getAndIncrement();
        if (id > 65535) {
            id = 1;
            nextReqId.set(2);
        }
        return id;
    }

    // ==================== Active Services ====================

    /**
     * Tracks an in-progress service (e.g., a blocking GetDataValues request).
     *
     * @param reqId   the request ID
     * @param context service-specific context
     */
    public void addActiveService(int reqId, Object context) {
        activeServices.put(reqId, context);
    }

    public Object removeActiveService(int reqId) {
        return activeServices.remove(reqId);
    }

    public Map<Integer, Object> getActiveServices() {
        return activeServices;
    }

    // ==================== Negotiation ====================

    public String getNegotiatedApduSize() {
        return negotiatedApduSize;
    }

    public void setNegotiatedApduSize(String negotiatedApduSize) {
        this.negotiatedApduSize = negotiatedApduSize;
    }

    public String getNegotiatedAsduSize() {
        return negotiatedAsduSize;
    }

    public void setNegotiatedAsduSize(String negotiatedAsduSize) {
        this.negotiatedAsduSize = negotiatedAsduSize;
    }

    public String getNegotiatedVersion() {
        return negotiatedVersion;
    }

    public void setNegotiatedVersion(String negotiatedVersion) {
        this.negotiatedVersion = negotiatedVersion;
    }

    // ==================== Listeners ====================

    public interface SessionListener {
        void onSessionClosed(CmsServerSession session);
    }

    public void addListener(SessionListener listener) {
        listeners.add(listener);
    }

    public void removeListener(SessionListener listener) {
        listeners.remove(listener);
    }

    // ==================== Lifecycle ====================

    /**
     * Closes the connection to the client.
     */
    public void close() {
        getConnection().close();
    }

    @Override
    public void onDisconnected() {
        super.onDisconnected();
        for (SessionListener l : listeners) {
            l.onSessionClosed(this);
        }
        listeners.clear();
    }
}
