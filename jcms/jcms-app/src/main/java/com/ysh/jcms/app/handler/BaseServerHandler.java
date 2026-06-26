package com.ysh.jcms.app.handler;

import com.ysh.jcms.app.node.InnerServer;
import com.ysh.jcms.utils.scl.model.document.SclDocument;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.frame.FrameHeader;
import com.ysh.jcms.utils.transport.service.ServiceHandler;
import com.ysh.jcms.utils.transport.session.Session;

/**
 * Base class for server-side service handlers.
 *
 * <p>Implements {@link ServiceHandler} and provides:
 * <ul>
 *   <li>Common error/success response building</li>
 *   <li>PDU decode with logging</li>
 * </ul>
 *
 * <p>Subclasses must implement:
 * <ul>
 *   <li>{@link #handleRequest(Session, Frame)} — process the request and return a response</li>
 * </ul>
 */
public abstract class BaseServerHandler extends BaseHandler implements ServiceHandler {

    private final ServiceName serviceName;

    protected BaseServerHandler(ServiceName serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public final ServiceName getServiceName() {
        return serviceName;
    }

    /**
     * Build a success response frame.
     *
     * @param respBytes encoded response PDU bytes
     * @param reqId     request ID from the request
     */
    protected Frame buildSuccess(byte[] respBytes, int reqId) {
        return new Frame(
            new FrameHeader().serviceCode(getServiceName()).resp(true).err(false),
            respBytes, reqId
        );
    }

    /**
     * Build an error response frame (err flag set to true).
     *
     * @param respBytes encoded error PDU bytes
     * @param reqId     request ID from the request
     */
    protected Frame buildError(byte[] respBytes, int reqId) {
        return new Frame(
            new FrameHeader().serviceCode(getServiceName()).resp(true).err(true),
            respBytes, reqId
        );
    }

    /**
     * Convenience: build a "no response" return for one-way messages.
     */
    protected static Frame noResponse() {
        return null;
    }

    // ──────────────────────────────────────────────
    //  SCL model access
    // ──────────────────────────────────────────────

    /**
     * Get the SCL server model from a session, if available.
     */
    protected SclServer getSclServer(Session session) {
        if (session instanceof InnerServer.ServerSession) {
            return ((InnerServer.ServerSession) session).getSclServer();
        }
        return null;
    }

    /**
     * Get the SCL document from a session, if available.
     */
    protected SclDocument getSclDocument(Session session) {
        if (session instanceof InnerServer.ServerSession) {
            return ((InnerServer.ServerSession) session).getSclDocument();
        }
        return null;
    }

    /**
     * Get the SCL access point from a session, if available.
     */
    protected SclAccessPoint getSclAccessPoint(Session session) {
        if (session instanceof InnerServer.ServerSession) {
            return ((InnerServer.ServerSession) session).getSclAccessPoint();
        }
        return null;
    }

    /**
     * Get the SCL DataTypeTemplates from a session, if available.
     */
    protected SclDataTypeTemplates getSclDataTypeTemplates(Session session) {
        if (session instanceof InnerServer.ServerSession) {
            return ((InnerServer.ServerSession) session).getSclDataTypeTemplates();
        }
        return null;
    }

    // ──────────────────────────────────────────────
    //  Decode helper
    // ──────────────────────────────────────────────

    /**
     * Decode PDU bytes into a CmsType, returning {@code true} on success.
     * On failure, the error is logged and {@code false} is returned.
     */
    protected <T extends com.ysh.jcms.core.CmsType> boolean tryDecode(Session session,
                                                                        Frame request,
                                                                        T pdu) {
        try {
            pdu.decode(request.asduBytes());
            return true;
        } catch (Exception e) {
            log.error("Failed to decode {} from {}: {}",
                pdu.getClass().getSimpleName(), session.getSessionId(), e.getMessage());
            return false;
        }
    }
}
