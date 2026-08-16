package com.ysh.jcms.app.handler.support;

import com.ysh.jcms.app.handler.base.BaseHandler;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

/**
 * Static helpers to decode a PDU from a response/error frame.
 * <p>
 * Extracted from {@link BaseClientHandler} — they are pure functions with no
 * instance state, so they live as a plain utility instead of bloating the base
 * class.
 */
public final class CmsFrameDecoder {

    private CmsFrameDecoder() {
    }

    /** Decode a PDU from a frame, wrapping decode failures as IOException. */
    public static <T extends CmsType> T decodeFrame(Frame frame, T pdu) throws IOException {
        if (frame == null)
            throw new IOException("Request timed out (no response)");
        try {
            pdu.decode(frame.asduBytes());
        } catch (Exception e) {
            throw new IOException("Failed to decode " + pdu.getClass().getSimpleName(), e);
        }
        return pdu;
    }

    /** Decode a response PDU from a frame and trace it. */
    public static <T extends CmsType> T decodeResp(Frame frame, T resp) throws IOException {
        if (frame == null)
            throw new IOException("Request timed out (no response)");
        resp.decode(frame.asduBytes());
        traceResp(resp);
        return resp;
    }

    /** Decode an error PDU from a frame. */
    public static <T extends CmsType> T decodeErr(Frame frame, T err) throws IOException {
        err.decode(frame.asduBytes());
        return err;
    }

    /** Trace a decoded response PDU. */
    public static void traceResp(CmsType resp) {
        BaseHandler.trace("<<<\n" + resp);
    }
}
