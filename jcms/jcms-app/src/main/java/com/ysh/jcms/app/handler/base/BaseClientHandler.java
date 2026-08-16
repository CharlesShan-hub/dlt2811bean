package com.ysh.jcms.app.handler.base;

import com.ysh.jcms.app.handler.support.CmsContent;
import com.ysh.jcms.app.handler.support.PaginationContext;
import com.ysh.jcms.app.handler.support.RequestExchange;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.frame.FrameHeader;
import com.ysh.jcms.utils.transport.session.SessionState;

import java.io.IOException;

public abstract class BaseClientHandler<D extends BaseDao> extends BaseHandler {

    // ── Fields ──────────────────────────────────────────────

    private CmsContent<D> currentContent;

    protected CmsNode node;

    /** Max auto-pull iterations to prevent infinite loops. */
    private static final int MAX_AUTO_PULL_ITERATIONS = 10000;

    // ── Constructors ────────────────────────────────────────

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

    // ── Execution context ───────────────────────────────────

    /** All client handlers must implement this as their entry point. */
    public abstract void execute(D dao) throws Exception;

    /**
     * Execute the client handler and populate the given {@link CmsContent} with the
     * response data and pagination context.
     * <p>
     * Console usage:
     *
     * <pre>
     * {
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

    // ── Session access ──────────────────────────────────────

    /** Next request ID from the client session. */
    protected int nextReqId() {
        return node.client().session().nextReqId();
    }

    /** Send a raw frame on the client connection. */
    protected void sendFrame(Frame frame) throws IOException {
        node.client().connection().send(frame);
    }

    /** Transition the client session to the given state. */
    protected void sessionState(SessionState state) {
        node.client().session().state(state);
    }

    /** Current association ID, or null if not associated. */
    protected byte[] associationId() {
        return node.client().session().associationId();
    }

    /** Mark the session as ASSOCIATED with the given association info. */
    protected void associateSession(byte[] assocId, String apRef, boolean secure) {
        node.client().session().associationId(assocId).associatedApRef(apRef).associatedSecure(secure).state(SessionState.ASSOCIATED);
    }

    // ── Send / one-way ──────────────────────────────────────

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

    /** Send a one-way (fire-and-forget) request built from the DAO. */
    protected void sendOneWay(CmsServiceInfo sc, D dao) throws IOException {
        if (node == null)
            throw new IOException("BaseClientHandler node not set");
        CmsType request = dao.request();
        trace(">>>\n" + request);
        sendFrame(new Frame(new FrameHeader().serviceCode(sc).resp(false).err(false), request.encode(), nextReqId()));
        onSuccess(null);
    }

    // ── Pagination internals ────────────────────────────────

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

    // ── Callbacks ───────────────────────────────────────────

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
}
