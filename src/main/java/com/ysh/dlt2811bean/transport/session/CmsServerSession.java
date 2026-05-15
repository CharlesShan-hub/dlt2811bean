package com.ysh.dlt2811bean.transport.session;

import com.ysh.dlt2811bean.scl2.model.SclAccessPoint;
import com.ysh.dlt2811bean.scl2.model.SclDataTypeTemplates;
import com.ysh.dlt2811bean.scl2.model.SclDocument;
import com.ysh.dlt2811bean.transport.io.CmsConnection;
import lombok.Getter;
import lombok.Setter;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class CmsServerSession extends CmsSession {

    private final CopyOnWriteArrayList<SessionListener> listeners = new CopyOnWriteArrayList<>();
    private final Map<Object, Object> attributes = new ConcurrentHashMap<>();

    @Setter
    private volatile String serverAccessPointReference;
    
    @Setter
    private volatile String iedName;
    
    @Setter
    private volatile String accessPointName;
    
    @Setter
    private volatile SclDocument sclDocument;
    
    @Setter
    private volatile SclAccessPoint sclAccessPoint;
    
    @Setter
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

    public Object getAttribute(Object key) { 
        return attributes.get(key); 
    }
    
    public void setAttribute(Object key, Object value) { 
        attributes.put(key, value); 
    }
    
    public Object removeAttribute(Object key) { 
        return attributes.remove(key); 
    }

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