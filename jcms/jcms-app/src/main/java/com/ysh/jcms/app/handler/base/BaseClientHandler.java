package com.ysh.jcms.app.handler.base;

import com.ysh.jcms.app.handler.support.CmsContent;
import com.ysh.jcms.app.handler.support.PaginationContext;
import com.ysh.jcms.app.handler.support.RequestExchange;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.frame.FrameHeader;

import java.io.IOException;

public abstract class BaseClientHandler<D extends BaseDao> extends BaseHandler {

    private CmsContent<D> currentContent;

    /**
     * Execute the client handler and populate the given {@link CmsContent} with the
     * response data and pagination context.
     * <p>
     * Console usage:
     *
     * <pre>
     * {
     *     &#64;code
     *     CmsContent<SvrDirDao> c = new CmsContent<>(new SvrDirDao());
     *     c.req().referenceAfter("...");
     *     console.getClient(SvrDirClient.class).executeResult(c);
     *     CmsPrinter.outputJson(c.res());
     * }
     * </pre>
     */
    public void executeResult(CmsContent<D> content) throws Exception {
        this.currentContent = content;
        try {
            execute(content.req());
        } finally {
            this.currentContent = null;
        }
    }

    /**
     * Returns the current {@link CmsContent} being executed. Available during
     * {@link #execute(BaseDao)} and its callbacks ({@code beforeAll},
     * {@code onSuccess}, {@code afterAll}). Returns a default empty instance when
     * called outside of an {@code executeResult} call, so that callers can safely
     * invoke {@code content().res(...)} without NPE.
     */
    protected CmsContent<D> content() {
        if (currentContent == null) {
            currentContent = new CmsContent<>(null);
        }
        return currentContent;
    }

    /** All client handlers must implement this as their entry point. */
    public abstract void execute(D dao) throws Exception;

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
        return node.client().session().nextReqId();
    }

    /** Max auto-pull iterations to prevent infinite loops. */
    private static final int MAX_AUTO_PULL_ITERATIONS = 10000;

    /**
     * Send a request built from the DAO. If {@link CmsContent#paginationContext()}
     * is available and {@link CmsContent#autoPull()} is true, automatically follows
     * {@code moreFollows} pagination.
     * <p>
     * Subclasses <b>must</b> call {@link PaginationContext#setLastMoreFollows} and
     * {@link PaginationContext#setLastReference} inside their
     * {@link #onSuccess(Frame, BaseDao)} override for auto-pull to work.
     */
    protected Frame send(CmsServiceInfo sc, D dao) throws IOException {
        CmsContent<D> content = content();
        PaginationContext ctx = content != null ? content.paginationContext() : null;
        beforeAll(dao);
        if (ctx != null) {
            return sendWithPagination(sc, dao, content);
        }
        // Simple non-paginated send
        Frame frame = newExchange(sc, dao).exchange();
        onSuccess(frame, dao);
        afterAll(dao);
        return frame;
    }

    /**
     * Build a {@link RequestExchange} for this handler's node/DAO pair. The error
     * decoder routes negative responses through {@link #onError(Frame)} so the
     * hook stays active on every path.
     */
    private RequestExchange newExchange(CmsServiceInfo sc, D dao) {
        return new RequestExchange(node, sc, dao, frame -> {
            onError(frame);
            return "Request failed for " + sc;
        });
    }

    /**
     * Internal pagination loop. Uses {@link RequestExchange} to send one request
     * per page and decode responses. Calls {@link #onSuccess(Frame, BaseDao)} for
     * each page, and {@link #setPaginationCursor(BaseDao, String)} to advance the
     * cursor.
     */
    private Frame sendWithPagination(CmsServiceInfo sc, D dao, CmsContent<D> content) throws IOException {
        PaginationContext ctx = content.paginationContext();
        ctx.setLastMoreFollows(false);
        ctx.setLastReference(null);

        RequestExchange exchange = newExchange(sc, dao);

        Frame frame = null;
        int iterations = 0;
        while (true) {
            if (++iterations > MAX_AUTO_PULL_ITERATIONS) {
                log.warn("Auto-pull exceeded {} iterations for {}, aborting", MAX_AUTO_PULL_ITERATIONS, sc);
                break;
            }
            frame = exchange.exchange();
            onSuccess(frame, dao);
            if (content.autoPull() && ctx.isLastMoreFollows()) {
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
        }
        afterAll(dao);
        return frame;
    }

    /**
     * Bridge method for subclasses to set the pagination cursor on their specific
     * DAO type. Default is no-op; subclasses that support pagination must override
     * to call {@code dao.referenceAfter(cursor)}.
     */
    protected void setPaginationCursor(D dao, String cursor) {
        // default no-op — override in subclasses that support pagination
    }

    /**
     * Send a request (encoded bytes). Subclasses should prefer
     * {@link #send(CmsServiceInfo, CmsType)} for automatic PDU tracing.
     */
    protected Frame send(CmsServiceInfo sc, byte[] pduBytes) throws IOException {
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
    protected Frame send(CmsServiceInfo sc, CmsType requestObject) throws IOException {
        trace(">>>\n" + requestObject);
        return send(sc, requestObject.encode());
    }

    /**
     * Send a one-way (fire-and-forget) frame. No response expected.
     */
    protected void sendOneWay(CmsServiceInfo sc, byte[] pduBytes) throws IOException {
        if (node == null)
            throw new IOException("BaseClientHandler node not set");
        trace(">>> " + sc + " (one-way)");
        node.client().connection().send(new Frame(new FrameHeader().serviceCode(sc).resp(false).err(false), pduBytes, nextReqId()));
        onSuccess(null);
    }

    /** Send a one-way (fire-and-forget) request object. */
    protected void sendOneWay(CmsServiceInfo sc, CmsType requestObject) throws IOException {
        trace(">>>\n" + requestObject);
        sendOneWay(sc, requestObject.encode());
    }

    /** Send a one-way (fire-and-forget) request built from the DAO. */
    protected void sendOneWay(CmsServiceInfo sc, D dao) throws IOException {
        sendOneWay(sc, dao.toRequest());
    }

    /** Trace a decoded response PDU. */
    protected static void traceResp(CmsType resp) {
        trace("<<<\n" + resp);
    }

    /**
     * Called once before the first request is sent (both paginated and
     * non-paginated). Default no-op. Override to set up DAO state before the
     * request loop.
     */
    protected void beforeAll(D dao) throws IOException {
    }

    /**
     * Called once after all pages are fetched (both paginated and non-paginated).
     * Default no-op. Override for post-pagination work (e.g. initServerDir, log).
     */
    protected void afterAll(D dao) throws IOException {
    }

    protected void onSuccess(Frame frame) throws IOException {
    }

    /**
     * Callback invoked after each successful response when using
     * {@link #send(CmsServiceInfo, BaseDao)}. Provides both the response frame and
     * the DAO so subclasses can store decoded results directly on the DAO.
     * <p>
     * For paginated requests, the {@link PaginationContext} is available via
     * {@link CmsContent#paginationContext()}. Default implementation delegates to
     * {@link #onSuccess(Frame)}.
     */
    protected void onSuccess(Frame frame, D dao) throws IOException {
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
