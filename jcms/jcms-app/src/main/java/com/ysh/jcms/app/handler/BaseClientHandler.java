package com.ysh.jcms.app.handler;

import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.frame.FrameHeader;

import java.io.IOException;

public abstract class BaseClientHandler<D extends BaseDao> extends BaseHandler {

    /** All client handlers must implement this as their entry point. */
    public abstract void execute(D dao) throws Exception;

    /**
     * Execute with a pagination context. Default implementation delegates to
     * {@link #execute(BaseDao)}. Subclasses that support pagination should override
     * this and use the context for state passing.
     */
    public void execute(D dao, PaginationContext ctx) throws Exception {
        execute(dao);
    }

    protected CmsNode node;

    protected BaseClientHandler() {
    }

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

    /** Max auto-pull iterations to prevent infinite loops. */
    private static final int MAX_AUTO_PULL_ITERATIONS = 10000;

    /**
     * Send a request built from the DAO with a pagination context for state
     * passing. If {@link BaseDao#autoPull()} is true, automatically follows
     * {@code moreFollows} pagination.
     * <p>
     * Subclasses <b>must</b> call {@link PaginationContext#setLastMoreFollows} and
     * {@link PaginationContext#setLastReference} inside their
     * {@link #onSuccess(Frame, PaginationContext)} override for auto-pull to work.
     */
    protected Frame send(ServiceName sc, D dao, PaginationContext ctx) throws IOException {
        ctx.setLastMoreFollows(false);
        ctx.setLastReference(null);
        Frame frame = null;
        int iterations = 0;
        do {
            if (++iterations > MAX_AUTO_PULL_ITERATIONS) {
                log.warn("Auto-pull exceeded {} iterations for {}, aborting", MAX_AUTO_PULL_ITERATIONS, sc);
                break;
            }
            byte[] pdu = dao.toRequest().encode();
            trace(">>>\n" + dao.toRequest());
            frame = sendPdu(sc, pdu, ctx);
            if (frame == null || frame.header().err())
                break;
            if (dao.autoPull() && ctx.isLastMoreFollows()) {
                String ref = ctx.getLastReference();
                if (ref == null || ref.isEmpty()) {
                    log.warn("Auto-pull: lastReference is null/empty for {}, stopping", sc);
                    break;
                }
                if (iterations > 1) {
                    log.debug("Auto-pull iteration {}: lastReference={}", iterations, ref);
                }
                setPaginationCursor(dao, ref);
            } else {
                break;
            }
        } while (true);
        return frame;
    }

    /**
     * Send raw PDU bytes and invoke the ctx-aware callback instead of the no-arg
     * {@link #onSuccess(Frame)}.
     */
    private Frame sendPdu(ServiceName sc, byte[] pduBytes, PaginationContext ctx) throws IOException {
        if (node == null)
            throw new IOException("BaseClientHandler node not set");
        Frame frame = node.sendRequest(sc, pduBytes);
        if (frame == null)
            throw new IOException("Request timed out for " + sc);
        if (frame.header().err())
            onError(frame);
        onSuccess(frame, ctx);
        return frame;
    }

    /**
     * Send a request without a pagination context. Creates an internal context and
     * calls {@link #send(ServiceName, BaseDao, PaginationContext)}.
     * <p>
     * Note: without a context, auto-pull state is not accessible to the caller.
     * Prefer using the ctx-parameterized version for paginated services.
     */
    protected Frame send(ServiceName sc, D dao) throws IOException {
        return send(sc, dao, new PaginationContext());
    }

    /**
     * Bridge method for subclasses to set the pagination cursor on their specific
     * DAO type. Default is no-op; subclasses that support auto-pull must override
     * to call {@code dao.referenceAfter(cursor)}.
     */
    protected void setPaginationCursor(D dao, String cursor) {
        // default no-op — override in subclasses that support pagination
    }

    /**
     * Send a request (encoded bytes). Subclasses should prefer
     * {@link #send(ServiceName, CmsType)} for automatic PDU tracing.
     */
    protected Frame send(ServiceName sc, byte[] pduBytes) throws IOException {
        if (node == null)
            throw new IOException("BaseClientHandler node not set");
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
     */
    protected Frame send(ServiceName sc, CmsType requestObject) throws IOException {
        trace(">>>\n" + requestObject);
        return send(sc, requestObject.encode());
    }

    /**
     * Send a one-way (fire-and-forget) frame. No response expected.
     */
    protected void sendOneWay(ServiceName sc, byte[] pduBytes) throws IOException {
        if (node == null)
            throw new IOException("BaseClientHandler node not set");
        trace(">>> " + sc + " (one-way)");
        node.getClient().getConnection().send(new Frame(new FrameHeader().serviceCode(sc).resp(false).err(false), pduBytes, nextReqId()));
        onSuccess(null);
    }

    /** Send a one-way (fire-and-forget) request object. */
    protected void sendOneWay(ServiceName sc, CmsType requestObject) throws IOException {
        trace(">>>\n" + requestObject);
        sendOneWay(sc, requestObject.encode());
    }

    /** Trace a decoded response PDU. */
    protected static void traceResp(CmsType resp) {
        trace("<<<\n" + resp);
    }

    protected void onSuccess(Frame frame) throws IOException {
    }

    /**
     * Callback invoked after each successful response during auto-pull. Default
     * implementation delegates to {@link #onSuccess(Frame)}. Subclasses that use a
     * pagination context should override this to populate the context with
     * accumulated data and pagination state.
     */
    protected void onSuccess(Frame frame, PaginationContext ctx) throws IOException {
        onSuccess(frame);
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
}
