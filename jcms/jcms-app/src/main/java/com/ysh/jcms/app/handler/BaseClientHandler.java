package com.ysh.jcms.app.handler;

import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.frame.FrameHeader;

import java.io.IOException;

public abstract class BaseClientHandler extends BaseHandler {

    protected CmsNode node;

    protected BaseClientHandler() {}

    protected BaseClientHandler(CmsNode node) {
        this.node = node;
    }

    /** Set node after no-arg construction (fluent). */
    public BaseClientHandler node(CmsNode node) {
        this.node = node;
        return this;
    }

    protected int nextReqId() {
        return node.getClient().getSession().nextReqId();
    }

    /**
     * Send a request (encoded bytes). Subclasses should prefer
     * {@link #send(ServiceName, CmsType)} for automatic PDU tracing.
     */
    protected Frame send(ServiceName sc, byte[] pduBytes) throws IOException {
        if (node == null) throw new IOException("BaseClientHandler node not set");
        Frame frame = node.sendRequest(sc, pduBytes);
        if (frame == null)
            throw new IOException("Request timed out for " + sc);
        if (frame.header().err())
            onError(frame);
        onSuccess(frame);
        return frame;
    }

    /**
     * Encode and send a request (object form), with PDU trace when enabled.
     * Response is NOT automatically traced here — each subclass calls
     * {@link #traceResp(CmsType)} inside {@link #onSuccess(Frame)} after decoding.
     */
    protected Frame send(ServiceName sc, CmsType requestObject) throws IOException {
        trace(">>>\n" + requestObject);
        return send(sc, requestObject.encode());
    }

    /**
     * Send a one-way (fire-and-forget) frame. No response expected. PDU is traced
     * when enabled.
     */
    protected void sendOneWay(ServiceName sc, byte[] pduBytes) throws IOException {
        if (node == null) throw new IOException("BaseClientHandler node not set");
        trace(">>> " + sc + " (one-way)");
        node.getClient().getConnection()
                .send(new Frame(new FrameHeader().serviceCode(sc).resp(false).err(false), pduBytes, reqIdFromBytes(pduBytes)));
        onSuccess(null);
    }

    /** Send a one-way (fire-and-forget) request object. */
    protected void sendOneWay(ServiceName sc, CmsType requestObject) throws IOException {
        trace(">>>\n" + requestObject);
        sendOneWay(sc, requestObject.encode());
    }

    /** Trace a decoded response PDU. Call from {@link #onSuccess(Frame)}. */
    protected static void traceResp(CmsType resp) {
        trace("<<<\n" + resp);
    }

    protected void onSuccess(Frame frame) throws IOException {
    }

    protected void onError(Frame frame) throws IOException {
        throw new IOException("Negative response for " + frame.header().serviceCode());
    }

    protected static <T extends CmsType> T decodeFrame(Frame frame, T pdu) throws IOException {
        if (frame == null)
            throw new IOException("Request timed out (no response)");
        try {
            pdu.decode(frame.asduBytes());
        } catch (Exception e) {
            throw new IOException("Failed to decode " + pdu.getClass().getSimpleName(), e);
        }
        return pdu;
    }

    /** Decode response PDU from frame and trace it. */
    protected static <T extends CmsType> T decodeResp(Frame frame, T resp) throws IOException {
        if (frame == null)
            throw new IOException("Request timed out (no response)");
        resp.decode(frame.asduBytes());
        traceResp(resp);
        return resp;
    }

    /** Decode error PDU from frame. */
    protected static <T extends CmsType> T decodeErr(Frame frame, T err) throws IOException {
        err.decode(frame.asduBytes());
        return err;
    }

    private static boolean isTraceEnabled() {
        return CmsConfigLoader.load().getClient().getConsole().isTracePdu();
    }

    protected static void trace(String msg) {
        if (isTraceEnabled()) {
            ConsolePrinter.gray("[TRACE] " + msg);
        }
    }
}
