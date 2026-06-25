package com.ysh.jcms.app.handler;

import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.frame.FrameHeader;

import java.io.IOException;

/**
 * Base class for programmatic client-side service handlers.
 *
 * <p>Provides common infrastructure:
 * <ul>
 *   <li>{@link CmsNode} reference for send/exchange operations</li>
 *   <li>{@link #nextReqId()} — session request ID generation</li>
 *   <li>One-way fire-and-forget {@link #sendOneWay(ServiceName, byte[])}</li>
 *   <li>Request-response {@link #send(ServiceName, byte[])} with PDU {@link #decodeFrame(Frame, CmsType)}</li>
 * </ul>
 *
 * <p><b>Subclass contract:</b> implement an {@code execute} method as the entry point.
 * The method is discovered by reflection in {@code CmsNode.execute()}.
 * Signatures vary per service, e.g.:
 * <pre>{@code
 * public void execute()                          // Release (no params, no return)
 * public void execute(AbortClientDao dao)        // Abort   (one-way, fire-and-forget)
 * public CmsAssociateResponse execute(AssociateClientDao dao)  // Associate (request-response)
 * }</pre>
 */
public abstract class BaseClientHandler extends BaseHandler {

    protected final CmsNode node;

    protected BaseClientHandler(CmsNode node) {
        this.node = node;
    }

    /**
     * Get the next request ID from the session.
     */
    protected int nextReqId() {
        return node.getClient().getSession().nextReqId();
    }

    /**
     * Send a one-way (fire-and-forget) frame. No response is expected.
     */
    protected void sendOneWay(ServiceName sc, byte[] pduBytes) throws IOException {
        int reqId = reqIdFromBytes(pduBytes);
        node.getClient().getConnection().send(new Frame(
            new FrameHeader().serviceCode(sc).resp(false).err(false),
            pduBytes, reqId
        ));
    }

    /**
     * Send a request frame and wait for the response.
     *
     * @return the response {@link Frame}, or {@code null} on timeout.
     */
    protected Frame send(ServiceName sc, byte[] pduBytes) throws IOException {
        return node.sendRequest(sc, pduBytes);
    }

    /**
     * Decode the ASDU bytes from a response frame into a PDU.
     *
     * @param frame response frame ({@code null} means timeout)
     * @param pdu   PDU instance to decode into
     * @param <T>   PDU type
     * @return the decoded PDU (same instance as {@code pdu})
     * @throws IOException if the frame is null or decode fails
     */
    protected static <T extends CmsType> T decodeFrame(Frame frame, T pdu) throws IOException {
        if (frame == null) throw new IOException("Request timed out (no response)");
        try {
            pdu.decode(frame.asduBytes());
        } catch (Exception e) {
            throw new IOException("Failed to decode " + pdu.getClass().getSimpleName(), e);
        }
        return pdu;
    }
}
