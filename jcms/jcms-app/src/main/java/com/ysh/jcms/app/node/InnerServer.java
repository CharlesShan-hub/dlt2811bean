package com.ysh.jcms.app.node;

import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.service.DispatchResult;
import com.ysh.jcms.utils.transport.service.Dispatcher;
import com.ysh.jcms.utils.transport.service.ServiceHandler;
import com.ysh.jcms.utils.transport.session.Session;
import com.ysh.jcms.utils.transport.session.SessionState;
import com.ysh.jcms.utils.transport.wire.Connection;
import com.ysh.jcms.utils.transport.wire.ConnectionListener;
import com.ysh.jcms.utils.transport.wire.ServerAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

public class InnerServer implements ConnectionListener {

    private static final Logger log = LoggerFactory.getLogger(InnerServer.class);

    private final int port;
    private final ServerAcceptor acceptor;
    private final Dispatcher dispatcher = new Dispatcher();
    private final CopyOnWriteArrayList<ServerSession> sessions = new CopyOnWriteArrayList<>();

    public InnerServer(int port) {
        this.port = port;
        this.acceptor = new ServerAcceptor(port, this);
    }

    public void register(ServiceHandler handler) {
        dispatcher.register(handler);
    }

    public void start() throws IOException {
        acceptor.start();
        log.info("InnerServer started on port {}", port);
    }

    public void stop() {
        acceptor.stop();
        for (ServerSession ss : sessions) ss.close();
        sessions.clear();
    }

    public int getPort() { return port; }
    public boolean isRunning() { return acceptor.isRunning(); }
    public java.util.List<ServerSession> getSessions() { return new java.util.ArrayList<>(sessions); }

    @Override
    public void onConnected(Connection connection) {
        ServerSession ss = new ServerSession(connection);
        sessions.add(ss);
    }

    @Override
    public void onFrameReceived(Connection connection, Frame frame) {
        ServerSession ss = findSession(connection);
        if (ss == null) return;

        Dispatcher.DispatchOutcome outcome = dispatcher.dispatch(ss, frame);
        switch (outcome.getResult()) {
            case HANDLED:
                if (outcome.getResponse() != null) {
                    try { connection.send(outcome.getResponse()); }
                    catch (IOException e) { log.error("Send response failed", e); }
                }
                break;
            case NOT_REGISTERED:
                log.warn("No handler for service: {}", frame.header().serviceCode());
                break;
            case ERROR_OCCURRED:
                log.error("Handler error for service: {}", frame.header().serviceCode());
                break;
        }
    }

    @Override
    public void onDisconnected(Connection connection) {
        ServerSession ss = findSession(connection);
        if (ss != null) {
            ss.setState(SessionState.DISCONNECTED);
            sessions.remove(ss);
        }
    }

    @Override
    public void onError(Connection connection, Exception e) {
        log.error("Connection error", e);
    }

    private ServerSession findSession(Connection connection) {
        for (ServerSession ss : sessions) {
            if (ss.getConnection() == connection) return ss;
        }
        return null;
    }

    public static class ServerSession extends Session {
        public ServerSession(Connection connection) {
            super("srv-" + connection.getSocket().getPort(), connection);
        }
        public void close() { getConnection().close(); }
    }
}
