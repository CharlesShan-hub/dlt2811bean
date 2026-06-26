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
        onSuccess(null);
    }

    /**
     * Send a request, check for timeout, and handle negative response.
     *
     * <p>On success, calls {@link #onSuccess(Frame)} before returning.
     * On error, calls {@link #onError(Frame)} which throws.</p>
     *
     * @param sc        service name
     * @param pduBytes  encoded request PDU
     * @return the response {@link Frame} (err=false, non-null)
     * @throws IOException on timeout or negative response
     */
    protected Frame send(ServiceName sc, byte[] pduBytes) throws IOException {
        Frame frame = node.sendRequest(sc, pduBytes);
        if (frame == null) throw new IOException("Request timed out for " + sc);
        if (frame.header().err()) onError(frame);
        onSuccess(frame);
        return frame;
    }

    /**
     * Handle a positive response (err=false). Default no-op.
     *
     * <p>Subclasses override to decode the success PDU, update session,
     * log completion, etc.</p>
     */
    protected void onSuccess(Frame frame) throws IOException {
    }

    /**
     * Handle a negative response (err flag set).
     *
     * <p>Default throws a generic IOException. Subclasses with a distinct
     * error PDU type should override to decode their error and throw.</p>
     */
    protected void onError(Frame frame) throws IOException {
        throw new IOException("Negative response for " + frame.header().serviceCode());
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
