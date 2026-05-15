package com.ysh.dlt2811bean.transport.session;

import com.ysh.dlt2811bean.scl2.model.SclAccessPoint;
import com.ysh.dlt2811bean.scl2.model.SclDataTypeTemplates;
import com.ysh.dlt2811bean.transport.io.CmsConnection;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class CmsServerSession extends CmsSession {

    private final CopyOnWriteArrayList<SessionListener> listeners = new CopyOnWriteArrayList<>();
    private final Map<Object, Object> attributes = new ConcurrentHashMap<>();

    private volatile String serverAccessPointReference;
    private volatile String iedName;
    private volatile String accessPointName;
    private volatile SclAccessPoint sclAccessPoint;
    private volatile SclDataTypeTemplates sclDataTypeTemplates;

    public CmsServerSession(CmsConnection connection) {
        super("srv-" + connection.getSocket().getPort(), connection);
    }

    public InetSocketAddress getClientAddress() {
        return (InetSocketAddress) getConnection().getSocket().getRemoteSocketAddress();
    }

    public interface SessionListener {
        void onSessionClosed(CmsServerSession session);
    }

    public void addListener(SessionListener listener) {
        listeners.add(listener);
    }

    public void removeListener(SessionListener listener) {
        listeners.remove(listener);
    }

    public void setAccessPoint(String serverAccessPointReference, String iedName,
                               String accessPointName, SclAccessPoint sclAccessPoint) {
        this.serverAccessPointReference = serverAccessPointReference;
        this.iedName = iedName;
        this.accessPointName = accessPointName;
        this.sclAccessPoint = sclAccessPoint;
    }

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

    public SclAccessPoint getSclAccessPoint() {
        return sclAccessPoint;
    }

    public SclDataTypeTemplates getSclDataTypeTemplates() {
        return sclDataTypeTemplates;
    }

    public void setSclDataTypeTemplates(SclDataTypeTemplates templates) {
        this.sclDataTypeTemplates = templates;
    }

    public Object getAttribute(Object key) { return attributes.get(key); }
    public void setAttribute(Object key, Object value) { attributes.put(key, value); }
    public Object removeAttribute(Object key) { return attributes.remove(key); }

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
