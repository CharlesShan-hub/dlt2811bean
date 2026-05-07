package com.ysh.dlt2811bean.transport.session;

import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.transport.io.CmsConnection;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Server-side session.
 *
 * <p>One instance exists per connected client. Manages the session lifecycle,
 * association ID, and the SCL AccessPoint model for the associated client.
 */
public class CmsServerSession extends CmsSession {

    //private static final Logger log = LoggerFactory.getLogger(CmsServerSession.class);

    private final CopyOnWriteArrayList<SessionListener> listeners = new CopyOnWriteArrayList<>();
    private final Map<Object, Object> attributes = new ConcurrentHashMap<>();

    // ==================== AccessPoint (set on Associate) ====================

    private volatile String serverAccessPointReference;
    private volatile String iedName;
    private volatile String accessPointName;
    private volatile SclIED.SclAccessPoint sclAccessPoint;

    public CmsServerSession(CmsConnection connection) {
        super("srv-" + connection.getSocket().getPort(), connection);
    }

    // ==================== Identity ====================

    public InetSocketAddress getClientAddress() {
        return (InetSocketAddress) getConnection().getSocket().getRemoteSocketAddress();
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

    // ==================== AccessPoint ====================

    /**
     * Sets the AccessPoint information for this session.
     * Called by AssociateHandler when association is successful.
     */
    public void setAccessPoint(String serverAccessPointReference, String iedName,
                               String accessPointName, SclIED.SclAccessPoint sclAccessPoint) {
        this.serverAccessPointReference = serverAccessPointReference;
        this.iedName = iedName;
        this.accessPointName = accessPointName;
        this.sclAccessPoint = sclAccessPoint;
    }

    /**
     * Clears the AccessPoint information for this session.
     * Called by ReleaseHandler or AbortHandler.
     */
    public void clearAccessPoint() {
        this.serverAccessPointReference = null;
        this.iedName = null;
        this.accessPointName = null;
        this.sclAccessPoint = null;
    }

    public String getServerAccessPointReference() {
        return serverAccessPointReference;
    }

    public String getIedName() {
        return iedName;
    }

    public String getAccessPointName() {
        return accessPointName;
    }

    /**
     * Returns the SCL AccessPoint model for this session.
     * Contains the Server with its LDevices and data model.
     */
    public SclIED.SclAccessPoint getSclAccessPoint() {
        return sclAccessPoint;
    }

    // ==================== Attributes ====================

    public Object getAttribute(Object key) { return attributes.get(key); }
    public void setAttribute(Object key, Object value) { attributes.put(key, value); }
    public Object removeAttribute(Object key) { return attributes.remove(key); }

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
        clearAccessPoint();
        attributes.clear();
        for (SessionListener l : listeners) {
            l.onSessionClosed(this);
        }
        listeners.clear();
    }
}
