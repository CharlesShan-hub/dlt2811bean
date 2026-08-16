package com.ysh.jcms.app.handler.support;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.base.BaseDao;

/**
 * Request/response result container passed between the Client and the Console.
 * <p>
 * The Console creates a {@code CmsContent} holding a DAO and calls
 * {@link BaseClientHandler#executeResult(CmsContent)}; after execution the
 * response data is read via {@link #res()}.
 * <p>
 * Besides {@code req} (the DAO carrying request parameters), all execution state
 * (autoPull, pagination context, response data) lives here; the DAO only builds
 * the request object via {@link BaseDao#toRequest()}.
 *
 * <pre>
 * {
 *     CmsContent<SvrDirDao> c = new CmsContent<>(new SvrDirDao().referenceAfter("..."), "true");
 *     console.getClient(SvrDirClient.class).executeResult(c);
 *     CmsPrinter.outputJson(c.res());
 * }
 * </pre>
 */
public class CmsContent<D extends BaseDao> {

    // ── Fields ──────────────────────────────────────────────

    private final D req;
    private Object res;
    private PaginationContext paginationContext;
    private boolean autoPull;

    // ── Constructors ────────────────────────────────────────

    public CmsContent(D req) {
        this.req = req;
    }

    /** Create with req and auto-pull flag from a CLI argument string. */
    public CmsContent(D req, String autoPull) {
        this.req = req;
        this.autoPull = "true".equalsIgnoreCase(autoPull);
    }

    // ── Request / response ──────────────────────────────────

    /** The request DAO (carries request parameters). */
    public D req() {
        return req;
    }

    /** Response data, populated by {@link BaseClientHandler#executeResult}. */
    public Object res() {
        return res;
    }

    /** Set response data (called by {@code BaseClientHandler}). */
    public void res(Object res) {
        this.res = res;
    }

    // ── Pagination ──────────────────────────────────────────

    /** Pagination context for auto-pull support. */
    public PaginationContext paginationContext() {
        if (paginationContext == null) {
            paginationContext = new PaginationContext();
        }
        return paginationContext;
    }

    /** Set pagination context (called by {@code BaseClientHandler}). */
    void paginationContext(PaginationContext paginationContext) {
        this.paginationContext = paginationContext;
    }

    /** Whether the client should automatically follow moreFollows pagination. */
    public boolean autoPull() {
        return autoPull;
    }

    /** Enable auto-pull. */
    public void autoPull(boolean autoPull) {
        this.autoPull = autoPull;
    }

    /**
     * Enable auto-pull from a CLI argument string. Accepts "true"
     * (case-insensitive); any other value (including null) is treated as false.
     */
    public void autoPull(String autoPull) {
        this.autoPull = "true".equalsIgnoreCase(autoPull);
    }

    /** True if the last pagination response indicated more data follows. */
    public boolean moreFollows() {
        return paginationContext != null && paginationContext.isLastMoreFollows();
    }
}
